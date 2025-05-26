//package tech.qiantong.qknow.module.ext.test;
//
//import org.neo4j.driver.*;
//import org.neo4j.driver.exceptions.Neo4jException;
//import org.neo4j.driver.types.Node;
//import org.neo4j.driver.types.Relationship;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class TestNeo4j {
//
//    public static void main(String[] args) {
//        // Neo4j 驱动配置
//        String uri = "bolt://localhost:7687"; // Neo4j 服务的 URI，默认是 bolt://localhost:7687
//        String username = "neo4j";            // Neo4j 用户名
//        String password = "123456";         // Neo4j 密码
//
//        // 创建 Neo4j 驱动实例
//        try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password))) {
//
//            // 创建会话
//            try (Session session = driver.session()) {
//
//                // 执行 Cypher 查询来获取三元组数据
//                String cypherQuery = "MATCH (a:KmcExtraction)-[r]->(b:KmcExtraction) " +
//                        "RETURN a.name AS head, type(r) AS relation, b.name AS tail, r.confidence AS confidence";
//
//                List<Record> result = session.run(cypherQuery).list();
//
//                // 遍历查询结果并输出
//                for (Record record : result) {
//                    String head = record.get("head").asString();
//                    String relation = record.get("relation").asString();
//                    String tail = record.get("tail").asString();
//                    String confidence = record.get("confidence").asString();
//
//                    System.out.println("Head: " + head + ", Relation: " + relation + ", Tail: " + tail + ", Confidence: " + confidence);
//                }
//
//            } catch (Neo4jException e) {
//                System.err.println("Query execution failed: " + e.getMessage());
//            }
//
//        } catch (Exception e) {
//            System.err.println("Failed to connect to Neo4j: " + e.getMessage());
//        }
//    }
//
////    public static void main(String[] args) {
////        // 连接到 Neo4j 数据库
////        String uri = "bolt://localhost:7687"; // Neo4j 的地址和端口
////        String user = "neo4j"; // 用户名
////        String password = "123456"; // 密码
////
////        // 创建 Neo4j 驱动程序
////        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
////        try (Session session = driver.session()) {
////
////            ArrayList<Object> arrayList = new ArrayList<>();
////            ArrayList<Object> arrayListR = new ArrayList<>();
////            String query = "MATCH (a:KmcExtraction)-[r]->(b:KmcExtraction)\n" +
////                    "RETURN a, r, b;";
////            Result result = driver.session().run(query);
////            if (!result.hasNext()) {
////                System.out.println("11111111");
////            } else {
////                while (result.hasNext()) {
////                    Record record = result.next();
////                    // 获取返回的节点
////                    Node node = record.get("a").asNode();
////                    Map<String, Object> objectMap = node.asMap();
////                    arrayList.add(node.asMap());
////
////                    Node node1 = record.get("b").asNode();
////                    Map<String, Object> objectMap1 = node1.asMap();
////
////                    Relationship relationship = record.get("r").asRelationship();
////                    arrayListR.add(relationship);
////                }
////                System.out.println(arrayList);
////            }
////
//////            String temp = "[{\"head\": \"人生长路\", \"tail\": \"刘德华\", \"relation\": \"歌手\", \"confidence\": \"0.47\"}," +
//////                    " {\"head\": \"人生长路\", \"tail\": \"男人的爱\", \"relation\": \"所属专辑\", \"confidence\": \"0.99\"}, " +
//////                    "{\"head\": \"人生长路\", \"tail\": \"李泉\", \"relation\": \"作词\", \"confidence\": \"0.56\"}, " +
//////                    "{\"head\": \"刘德华\", \"tail\": \"男人的爱\", \"relation\": \"歌手\", \"confidence\": \"0.75\"}, " +
//////                    "{\"head\": \"刘德华\", \"tail\": \"李泉\", \"relation\": \"作词\", \"confidence\": \"0.50\"}, " +
//////                    "{\"head\": \"男人的爱\", \"tail\": \"李泉\", \"relation\": \"作词\", \"confidence\": \"0.49\"}]";
//////
//////            List<KmcExtractionDO> extractionList = JSON.parseArray(temp, KmcExtractionDO.class);
//////
//////            // 遍历记录并存入数据库
//////            for (KmcExtractionDO extractionDO : extractionList) {
//////                // 创建或更新节点
//////                String createNodeQuery = "MERGE (a:KmcExtraction {name: $head}) " +
//////                        "MERGE (b:KmcExtraction {name: $tail}) " +
//////                        "MERGE (a)-[r:" + extractionDO.getRelation() + " {confidence: $confidence}]->(b)";
//////                session.run(createNodeQuery, parameters(
//////                        "head", extractionDO.getHead(),
//////                        "tail", extractionDO.getTail(),
//////                        "relation", extractionDO.getRelation(),
//////                        "confidence", extractionDO.getConfidence()
//////                ));
//////            }
//////
//////            System.out.println("数据已成功导入到 Neo4j!");
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        } finally {
////            // 关闭连接
////            driver.close();
////        }
////    }
//
////    public static void main(String[] args) {
////        // 设置数据库连接信息
////        String uri = "bolt://localhost:7687";  // Neo4j默认的连接URI
////        String user = "neo4j";  // 默认用户名
////        String password = "123456";  // 默认密码
////
////        // 创建驱动实例
////        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
////
////        // 使用驱动进行数据库操作
////        try (Session session = driver.session()) {
////            // 在此处执行查询或数据存储操作
////            storeData(session);
////        } finally {
////            // 关闭驱动
////            driver.close();
////        }
////    }
////
////    // 存储数据示例
////    public static void storeData(Session session) {
////        // 创建一个节点，并向该节点添加属性
////        session.writeTransaction(tx -> {
////            tx.run("CREATE (n:Person {name: 'Alice', age: 30})");
////            return null;
////        });
////
////        // 创建节点并与其他节点建立关系
////        session.writeTransaction(tx -> {
////            tx.run("CREATE (n:Person {name: 'Bob', age: 25})");
////            tx.run("MATCH (a:Person {name: 'Alice'}), (b:Person {name: 'Bob'}) " +
////                    "CREATE (a)-[:KNOWS]->(b)");
////            return null;
////        });
////
////        System.out.println("Data stored successfully!");
////    }
//
//}
