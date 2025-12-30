package tech.qiantong.qknow.common.database.dialect;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.core.DbColumn;
import tech.qiantong.qknow.common.database.core.DbName;
import tech.qiantong.qknow.common.database.core.DbTable;
import tech.qiantong.qknow.common.database.exception.DataQueryException;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class MongoDBDialect extends AbstractDbDialect {


    @Override
    public RowMapper<DbTable> tableMapper() {
        return null;
    }

    @Override
    public RowMapper<DbColumn> columnMapper() {
        return null;
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return null;
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return null;
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        return null;
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return null;
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        return null;
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return null;
    }

    @Override
    public String getDbName() {
        return null;
    }

    @Override
    public String getDbName(DbName dbName) {
        return null;
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        return null;
    }


    @Override
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        String url = dbQueryProperty.trainToJdbcUrl();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(url))
                .applyToClusterSettings(b -> b.serverSelectionTimeout(10, TimeUnit.SECONDS))
                .applyToSocketSettings(b -> b
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS))
                .build();

        try (MongoClient client = MongoClients.create(settings)) {
            String dbName = StringUtils.defaultIfBlank(
                    dbQueryProperty.getDbName(),
                    dbQueryProperty.getSid()
            );
            client.getDatabase(dbName).runCommand(new Document("ping", 1));
            return true;
        } catch (Exception e) {
            throw new DataQueryException("MongoDB 连接失败,稍后重试");
        }
    }

    public static List<DbTable> getTables(String dbName, MongoClient mongoClient) {
        MongoDatabase db = mongoClient.getDatabase(dbName);
        List<DbTable> tables = new ArrayList<>();
        for (org.bson.Document coll : db.listCollections()) {
            String name = coll.getString("name");
            if (name == null || name.startsWith("system.")) continue; // 可选：过滤系统集合

            String comment = "";
            Document options = coll.get("options", Document.class);
            if (options != null) {
                comment = options.getString("comment");
            }

            DbTable t = new DbTable();
            t.setTableName(name);
            t.setTableComment(comment);
            tables.add(t);
        }
        return tables;
    }

    public static List<DbColumn> getTableColumns(String dbName,
                                                 String tableName,
                                                 MongoClient mongoClient) {
        MongoDatabase db = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(tableName);

        List<DbColumn> columns = new ArrayList<>();
        // 取第一条文档来推断结构（也可做更多采样）
        Document first = collection.find().limit(1).first();
        if (first == null) {
            return columns; // 集合无数据，返回空
        }

        int pos = 1;
        for (Map.Entry<String, Object> entry : first.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();

            DbColumn col = DbColumn.builder()
                    .colName(field)
                    .dataType(value != null ? value.getClass().getSimpleName() : "Object")
                    .dataLength(null)        // 文档字段没有固定长度
                    .dataPrecision(null)     // 没有数值精度定义
                    .dataScale(null)         // 没有小数位定义
                    .colKey(false)           // Mongo 主键默认是 "_id"
                    .nullable(true)          // Mongo 字段都可以不存在
                    .colPosition(pos++)
                    .dataDefault(null)       // 没有默认值定义
                    .colComment("")          // 没有字段注释
                    .build();

            if ("_id".equals(field)) {
                col.setColKey(true); // MongoDB 的 "_id" 字段天然是主键
                col.setNullable(false);
            }

            columns.add(col);
        }

        return columns;
    }

    public static int generateCheckTableExistsSQL(String dbName,
                                                  String tableName,
                                                  MongoClient mongoClient) {
        MongoDatabase db = mongoClient.getDatabase(dbName);
        try (MongoCursor<Document> cur = db.listCollections()
                .filter(Filters.eq("name", tableName))
                .iterator()) {
            return cur.hasNext() ? 1 : 0;
        }
    }


    /**
     * 根据 DbColumn 列表创建集合并附加 JSON Schema 校验
     *
     * @param dbQueryProperty      数据库名
     * @param tableName   集合名
     * @param columns     列定义（DbColumn）
     * @param comment     集合注释（可为 null）
     * @param mongoClient MongoClient
     */
    public static void createCollectionWithSchema(DbQueryProperty dbQueryProperty,
                                                  String tableName,
                                                  List<DbColumn> columns,
                                                  String comment,
                                                  MongoClient mongoClient) {

        MongoDatabase db = mongoClient.getDatabase(dbQueryProperty.getDbName());


        // 1) 创建集合（存在则忽略）
        try {
            Document cmd = new Document("create", tableName);
            if (comment != null && !comment.isEmpty()) {
                cmd.append("comment", comment);
            }
            db.runCommand(cmd);
        } catch (Exception ignore) {
            try { db.createCollection(tableName); } catch (Exception ignore2) {}
        }

        MongoCollection<Document> col = db.getCollection(tableName);

        // 2) 如果集合已有数据，就不再插入默认数据
        if (col.countDocuments() > 0) return;

        // 3) 组装一条默认数据（优先 dataDefault，没有就 null）
        Map<String, Object> defaults = new HashMap<>();
        for (DbColumn c : columns) {
            String name = c.getColName();
            if (name == null || name.trim().isEmpty()) continue;

            Object value = parseDefaultValue(c);
            defaults.put(name, value);
        }

        // 4) 复用 addTableData 插入默认数据
        addTableData(tableName, dbQueryProperty, mongoClient, defaults);
    }

    /** 将 DbColumn.dataDefault 转为合适类型；无默认或"null"返回 null */
    private static Object parseDefaultValue(DbColumn c) {
        String def = c.getDataDefault();
        if (def == null) return null;
        String val = def.trim();
        if (val.isEmpty() || "null".equalsIgnoreCase(val)) return null;

        String dt = Optional.ofNullable(c.getDataType()).orElse("").toLowerCase(Locale.ROOT);

        try {
            // boolean
            if (dt.contains("bool")) {
                if ("1".equals(val) || "true".equalsIgnoreCase(val) || "t".equalsIgnoreCase(val) || "y".equalsIgnoreCase(val)) return true;
                if ("0".equals(val) || "false".equalsIgnoreCase(val) || "f".equalsIgnoreCase(val) || "n".equalsIgnoreCase(val)) return false;
                return Boolean.parseBoolean(val);
            }
            // 整数
            if (dt.equals("int") || dt.equals("integer") || dt.contains("smallint") || dt.contains("tinyint")) {
                return Integer.parseInt(val);
            }
            if (dt.contains("bigint") || dt.equals("long")) {
                return Long.parseLong(val);
            }
            // 浮点/小数
            if (dt.contains("double") || dt.contains("float") || dt.contains("real")) {
                return Double.parseDouble(val);
            }
            if (dt.startsWith("number") || dt.startsWith("numeric") || dt.startsWith("decimal")) {
                return new java.math.BigDecimal(val);
            }
            // 日期/时间
            if (dt.contains("date") || dt.contains("time")) {
                // 常见格式
                java.util.Date d = tryParseDate(val,
                        "yyyy-MM-dd HH:mm:ss",
                        "yyyy-MM-dd'T'HH:mm:ss.SSSX",
                        "yyyy-MM-dd",
                        "yyyy/MM/dd HH:mm:ss",
                        "yyyy/MM/dd");
                if (d != null) return d;
                // 时间戳
                if (val.matches("^\\d{13}$")) return new java.util.Date(Long.parseLong(val));
                if (val.matches("^\\d{10}$")) return new java.util.Date(Long.parseLong(val) * 1000);
                return null; // 解析不了就置空
            }
            // 其它：按字符串
            return val;
        } catch (Exception e) {
            // 解析失败回退为字符串，避免抛错
            return val;
        }
    }
    private static java.util.Date tryParseDate(String s, String... patterns) {
        for (String p : patterns) {
            try {
                java.text.SimpleDateFormat f = new java.text.SimpleDateFormat(p);
                f.setLenient(false);
                return f.parse(s);
            } catch (java.text.ParseException ignore) {}
        }
        return null;
    }

    /**
     * 关系型/通用 dataType 文本 -> MongoDB bsonType 映射
     */
    private static String mapToBsonType(String dataType, String colName) {
        if (colName != null && "_id".equals(colName)) {
            // _id 通常用 ObjectId；若你会自定义为字符串，可按需改这里
            return "objectId";
        }
        if (dataType == null) return null;
        String type = dataType.trim();


        switch (type) {
            case "char":
            case "CHAR":
            case "text":
            case "string":
            case "varchar2":
            case "VARCHAR":
            case "VARCHAR2":
            case "TEXT":
            case "CLOB":
            case "varchar":
                return "string";
            case "int":
            case "integer":
            case "number":
            case "bigint":
            case "BIGINT":
            case "INT":
            case "INTEGER":
                return "int";
            case "double":
            case "float":
                return "double";
            case "decimal":
            case "DECIMAL":
            case "numeric":
            case "NUMERIC":
                return "decimal";
            case "bool":
                return "bool";
            case "date":
            case "time":
            case "DATE":
            case "DATETIME":
            case "TIMESTAMP":
                return "date";
            case "json":
            case "map":
            case "object":
            case "document":
                return "object";
            case "array":
            case "list":
                return "array";
            default:
                return null;
        }

    }


    public static List<Map<String, Object>> queryDbColumnByList(List<DbColumn> columns,
                                                                String tableName,
                                                                DbQueryProperty dbQueryProperty,
                                                                long offset,
                                                                long size,
                                                                MongoClient mongoClient, String where, List<Map> orderByList) {
        MongoDatabase db = mongoClient.getDatabase(dbQueryProperty.getDbName());
        MongoCollection<Document> collection = db.getCollection(tableName);

        // where 为空 → 不加过滤
        Bson filter = (where == null || where.trim().isEmpty()) ? null : MongoDBDialect.buildFilter(where);

        Document projection = buildProjection(columns);

        FindIterable<Document> iterable = (filter == null ? collection.find() : collection.find(filter))
                .skip((int) Math.max(0, offset))
                .limit((int) Math.max(0, size));

        if (!projection.isEmpty()) {
            iterable.projection(projection);
        }

        Document sortDoc = buildSort(orderByList);
        if (!sortDoc.isEmpty()) {
            iterable.sort(sortDoc);
        }

        // 3) 转换为 List<Map<String,Object>>
        List<Map<String, Object>> result = new ArrayList<>();
        for (Document doc : iterable) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (String key : doc.keySet()) {
                row.put(key, doc.get(key));
            }
            result.add(row);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private static Document buildSort(List<Map> orderByList) {
        Document sort = new Document();
        if (orderByList == null || orderByList.isEmpty()) return sort;

        for (Map item : orderByList) {
            if (item == null) continue;
            Object colObj = item.get("orderByColumn");
            if (colObj == null) continue;
            String col = String.valueOf(colObj).trim();
            if (col.isEmpty()) continue;

            String dir = String.valueOf(item.getOrDefault("isAsc", "asc")).trim().toLowerCase();
            // 兼容 "des" 写法
            int v = "desc".equals(dir) ? -1 : 1;
            sort.append(col, v);
        }
        return sort;
    }


    public static int countNew(String tableName,
                                       DbQueryProperty dbQueryProperty,
                                       MongoClient mongoClient,String where) {
        MongoDatabase db = mongoClient.getDatabase(dbQueryProperty.getDbName());
        MongoCollection<Document> collection = db.getCollection(tableName);

        try {
            Bson filter = (where == null || where.trim().isEmpty()) ? null : MongoDBDialect.buildFilter(where);
            long cnt = (filter == null) ? collection.countDocuments() : collection.countDocuments(filter);
            return (int) Math.min(cnt, Integer.MAX_VALUE);
        } catch (Exception e) {
            throw new DataQueryException("MongoDB 获取表数据量失败: " + e.getMessage());
        }
    }

    private static Document buildProjection(List<DbColumn> columns) {
        Document proj = new Document();
        if (columns != null) {
            for (DbColumn c : columns) {
                if (c != null && c.getColName() != null && !c.getColName().isEmpty()) {
                    proj.append(c.getColName(), 1);
                }
            }
        }
        return proj;
    }

    public static Bson buildFilter(String where) {
        if (where == null || where.trim().isEmpty()) return null;

        // 仅支持 AND 如需 OR，可再扩展解析器。
        String[] parts = where.trim()
                .replaceAll("(?i)\\s+and\\s+", " AND ") // 规范空白
                .split("\\s+AND\\s+");

        List<Bson> andList = new ArrayList<>();
        for (String raw : parts) {
            String cond = raw.trim();
            if (cond.isEmpty()) continue;

            Bson one = parseOneCondition(cond);
            if (one != null) andList.add(one);
        }
        if (andList.isEmpty()) return null;
        return andList.size() == 1 ? andList.get(0) : Filters.and(andList);
    }

    private static Bson parseOneCondition(String cond) {
        // IS [NOT] NULL
        var mNull = java.util.regex.Pattern.compile("(?i)^(\\w+)\\s+IS\\s+(NOT\\s+)?NULL$").matcher(cond);
        if (mNull.find()) {
            String field = mNull.group(1);
            boolean not = mNull.group(2) != null;
            return not ? Filters.ne(field, null) : Filters.eq(field, null);
        }

        // [NOT] IN (...)
        var mIn = java.util.regex.Pattern.compile("(?i)^(\\w+)\\s+(NOT\\s+)?IN\\s*\\((.*)\\)$").matcher(cond);
        if (mIn.find()) {
            String field = mIn.group(1);
            boolean not = mIn.group(2) != null;
            String body = mIn.group(3);
            List<Object> values = splitCsvValues(body).stream().map(MongoDBDialect::parseLiteral).collect(Collectors.toList());
            return not ? Filters.nin(field, values) : Filters.in(field, values);
        }

        // LIKE
        var mLike = java.util.regex.Pattern.compile("(?i)^(\\w+)\\s+LIKE\\s+(.+)$").matcher(cond);
        if (mLike.find()) {
            String field = mLike.group(1);
            String pat = stripQuotes(mLike.group(2).trim());
            // SQL LIKE -> 正则：% -> .*, _ -> .
            String regex = Pattern.quote(pat)
                    .replace("%", "\\E.*\\Q")
                    .replace("_", "\\E.\\Q");
            Pattern r = Pattern.compile("^" + regex + "$", Pattern.CASE_INSENSITIVE);
            return Filters.regex(field, r);
        }

        // 比较运算符：=, !=, <>, >=, <=, >, <
        var mCmp = java.util.regex.Pattern.compile("(?i)^(\\w+)\\s*(=|!=|<>|>=|<=|>|<)\\s*(.+)$").matcher(cond);
        if (mCmp.find()) {
            String field = mCmp.group(1);
            String op = mCmp.group(2);
            Object val = parseLiteral(mCmp.group(3).trim());
            switch (op) {
                case "=":  return Filters.eq(field, val);
                case "!=":
                case "<>": return Filters.ne(field, val);
                case ">":  return Filters.gt(field, val);
                case ">=": return Filters.gte(field, val);
                case "<":  return Filters.lt(field, val);
                case "<=": return Filters.lte(field, val);
                default:   return null;
            }
        }

        // 兜底：不识别则忽略该条件
        return null;
    }

    /** 解析字面量：去引号，识别 null/boolean/number/ObjectId */
    private static Object parseLiteral(String token) {
        String t = token.trim();

        // 去包裹引号（单/双）
        t = stripQuotes(t);

        // NULL
        if (t.equalsIgnoreCase("null")) return null;

        // 布尔
        if (t.equalsIgnoreCase("true"))  return Boolean.TRUE;
        if (t.equalsIgnoreCase("false")) return Boolean.FALSE;

        // ObjectId（_id 常见）
        if (t.matches("^[0-9a-fA-F]{24}$")) {
            try { return new ObjectId(t); } catch (Exception ignore) {}
        }

        // 数字（int / long / double）
        if (t.matches("^-?\\d+$")) {
            try {
                long lv = Long.parseLong(t);
                if (lv >= Integer.MIN_VALUE && lv <= Integer.MAX_VALUE) return (int) lv;
                return lv;
            } catch (NumberFormatException ignore) {}
        }
        if (t.matches("^-?\\d+\\.\\d+$")) {
            try { return Double.parseDouble(t); } catch (NumberFormatException ignore) {}
        }

        // 字符串
        return t;
    }

    /** 拆 IN(...) 的值，支持带引号的逗号 */
    private static List<String> splitCsvValues(String body) {
        List<String> arr = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inS = false, inD = false;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '\'' && !inD) { inS = !inS; sb.append(c); continue; }
            if (c == '"'  && !inS) { inD = !inD; sb.append(c); continue; }
            if (c == ',' && !inS && !inD) {
                arr.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) arr.add(sb.toString().trim());
        return arr;
    }

    private static String stripQuotes(String s) {
        if ((s.startsWith("'") && s.endsWith("'")) || (s.startsWith("\"") && s.endsWith("\""))) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    public static int addTableData(String tableName, DbQueryProperty dbQueryProperty, MongoClient mongoClient, Map<String, Object> after) {
        if (after == null || after.isEmpty()) {
            return 0;
        }
        try {
            MongoDatabase db = mongoClient.getDatabase(dbQueryProperty.getDbName());
            MongoCollection<Document> collection = db.getCollection(tableName);

            Document doc = new Document(after);
            InsertOneResult res = collection.insertOne(doc);

            // 写入被服务器确认则判定成功
            return (res != null && res.wasAcknowledged()) ? 1 : 0;
        } catch (Exception e) {
            // 这里不抛异常，按你的要求返回 0 表示失败
            return 0;
        }
    }

    /**
     *
     * @param tableName 表明
     * @param dbQueryProperty
     * @param mongoClient
     * @param after 最新字段数据
     * @param setCols  需要修改的字段名称
     * @param whereCols  当作where条件的字段名
     * @return
     */
    public static int updateTableData(
            String tableName
            , DbQueryProperty dbQueryProperty
            , MongoClient mongoClient
            , Map<String, Object> after
            , List<String> setCols, List<String> whereCols) {
        // 基本校验
        if (after == null || after.isEmpty()) return 0;
        if (setCols == null || setCols.isEmpty()) return 0;
        if (whereCols == null || whereCols.isEmpty()) return 0;

        try {
            MongoDatabase db = mongoClient.getDatabase(dbQueryProperty.getDbName());
            MongoCollection<Document> collection = db.getCollection(tableName);

            // 1) 构建 where 过滤
            List<Bson> andList = new ArrayList<>();
            for (String w : whereCols) {
                if (w == null || w.isEmpty()) continue;
                if (!after.containsKey(w)) return 0;
                Object v = normalizeValue(w, after.get(w));
                andList.add(Filters.eq(w, v));
            }
            if (andList.isEmpty()) return 0;
            Bson filter = (andList.size() == 1) ? andList.get(0) : Filters.and(andList);

            // 2) 构建 $set
            Document setDoc = new Document();
            for (String s : setCols) {
                if (s == null || s.isEmpty()) continue;
                if (!after.containsKey(s)) continue; // 未提供则跳过
                // 避免把 where 字段同时作为 set 字段更新（常见误用）
                if (whereCols.contains(s)) continue;
                Object v = normalizeValue(s, after.get(s));
                setDoc.append(s, v);
            }
            if (setDoc.isEmpty()) return 0;

            Document update = new Document("$set", setDoc);

            // 3) 执行更新（批量）
            UpdateResult res = collection.updateMany(filter, update);

            // 4) 成功判定：服务端确认 且（命中或修改了文档）
            return (res.wasAcknowledged() && (res.getModifiedCount() > 0 || res.getMatchedCount() > 0)) ? 1 : 0;

        } catch (Exception e) {
            return 0;
        }
    }

    /** 将常见字符串值规范化，例如把 _id 的 24位hex 转为 ObjectId */
    private static Object normalizeValue(String field, Object value) {
        if (value == null) return null;
        if ("_id".equals(field) && value instanceof String) {
            String s = (String) value;
            if (s.matches("^[0-9a-fA-F]{24}$")) {
                try { return new ObjectId(s); } catch (Exception ignore) {}
            }
        }
        return value;
    }

    /**
     * MongoDB 层级查询
     * @param dbName 当前节点（含 level、dbName）
     * @param mongoClient Mongo 客户端
     * @return 层级列表
     */
    public static List<DbName> getDbNames(DbName dbName, MongoClient mongoClient) {
        List<DbName> result = new ArrayList<>();

        // 第一层：数据库
        if (dbName.getLevel() == 1) {
            MongoIterable<String> dbList = mongoClient.listDatabaseNames();
            for (String name : dbList) {
                result.add(DbName.builder()
                        .dbName(name)
                        .level(1)
                        .totalLevels(2)
                        .build());
            }
        }
        // 第二层：集合（Collection）
        else if (dbName.getLevel() == 2) {
            if (dbName.getDbName() == null || dbName.getDbName().trim().isEmpty()) {
                throw new IllegalArgumentException("MongoDB level=2 需要上级数据库名");
            }
            MongoDatabase database = mongoClient.getDatabase(dbName.getDbName());
            MongoIterable<String> collections = database.listCollectionNames();
            for (String name : collections) {
                result.add(DbName.builder()
                        .dbName(name)
                        .level(2)
                        .totalLevels(2)
                        .build());
            }
        }
        else {
            throw new UnsupportedOperationException("MongoDB 仅支持 level=1~2");
        }

        return result;
    }
}
