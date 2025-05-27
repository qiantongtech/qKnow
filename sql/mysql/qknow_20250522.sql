-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists system_dept;
create table system_dept (
                          dept_id           bigint(20)      not null auto_increment    comment '部门id',
                          parent_id         bigint(20)      default 0                  comment '父部门id',
                          ancestors         varchar(50)     default ''                 comment '祖级列表',
                          dept_name         varchar(30)     default ''                 comment '部门名称',
                          order_num         int(4)          default 0                  comment '显示顺序',
                          leader            varchar(20)     default null               comment '负责人',
                          phone             varchar(11)     default null               comment '联系电话',
                          email             varchar(50)     default null               comment '邮箱',
                          status            char(1)         default '0'                comment '部门状态（0正常 1停用）',
                          del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
                          create_by         varchar(64)     default ''                 comment '创建者',
                          create_time 	    datetime                                   comment '创建时间',
                          update_by         varchar(64)     default ''                 comment '更新者',
                          update_time       datetime                                   comment '更新时间',
                          primary key (dept_id)
) engine=innodb auto_increment=200 comment = '部门表';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
insert into system_dept values(100,  0,   '0',          '千桐科技',   0, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', 'admin', sysdate(), '', null);
insert into system_dept values(101,  100, '0,100',      '南京总公司', 1, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', 'admin', sysdate(), '', null);
insert into system_dept values(102,  100, '0,100',      '郑州分公司', 2, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', 'admin', sysdate(), '', null);
insert into system_dept values(103,  101, '0,100,101',  '研发部门',   1, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', 'admin', sysdate(), '', null);
insert into system_dept values(104,  101, '0,100,101',  '市场部门',   2, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', 'admin', sysdate(), '', null);
insert into system_dept values(105,  101, '0,100,101',  '测试部门',   3, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', 'admin', sysdate(), '', null);
insert into system_dept values(106,  101, '0,100,101',  '财务部门',   4, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', 'admin', sysdate(), '', null);
insert into system_dept values(107,  101, '0,100,101',  '运维部门',   5, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', 'admin', sysdate(), '', null);
insert into system_dept values(108,  102, '0,100,102',  '市场部门',   1, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', 'admin', sysdate(), '', null);
insert into system_dept values(109,  102, '0,100,102',  '财务部门',   2, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', 'admin', sysdate(), '', null);


-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists system_user;
create table system_user (
                          user_id           bigint(20)      not null auto_increment    comment '用户ID',
                          dept_id           bigint(20)      default null               comment '部门ID',
                          user_name         varchar(30)     not null                   comment '用户账号',
                          nick_name         varchar(30)     not null                   comment '用户昵称',
                          user_type         varchar(2)      default '00'               comment '用户类型（00系统用户）',
                          email             varchar(50)     default ''                 comment '用户邮箱',
                          phonenumber       varchar(11)     default ''                 comment '手机号码',
                          sex               char(1)         default '0'                comment '用户性别（0男 1女 2未知）',
                          avatar            varchar(100)    default ''                 comment '头像地址',
                          password          varchar(100)    default ''                 comment '密码',
                          status            char(1)         default '0'                comment '账号状态（0正常 1停用）',
                          del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
                          login_ip          varchar(128)    default ''                 comment '最后登录IP',
                          login_date        datetime                                   comment '最后登录时间',
                          create_by         varchar(64)     default ''                 comment '创建者',
                          create_time       datetime                                   comment '创建时间',
                          update_by         varchar(64)     default ''                 comment '更新者',
                          update_time       datetime                                   comment '更新时间',
                          remark            varchar(500)    default null               comment '备注',
                          primary key (user_id)
) engine=innodb auto_increment=100 comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into system_user values(1,  103, 'qKnow', '千知', '00', 'support@qiantong.tech', '15888888888', '0', '', '$2a$10$M9QTlVS3URMVLDMMmJYYress8MgeKE0ahcNQSwO.T/TI8/U1U7pF6', '0', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', null, '管理员');


-- ----------------------------
-- 3、岗位信息表
-- ----------------------------
drop table if exists system_post;
create table system_post
(
    post_id       bigint(20)      not null auto_increment    comment '岗位ID',
    post_code     varchar(64)     not null                   comment '岗位编码',
    post_name     varchar(50)     not null                   comment '岗位名称',
    post_sort     int(4)          not null                   comment '显示顺序',
    status        char(1)         not null                   comment '状态（0正常 1停用）',
    create_by     varchar(64)     default ''                 comment '创建者',
    create_time   datetime                                   comment '创建时间',
    update_by     varchar(64)     default ''			       comment '更新者',
    update_time   datetime                                   comment '更新时间',
    remark        varchar(500)    default null               comment '备注',
    primary key (post_id)
) engine=innodb comment = '岗位信息表';

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
insert into system_post values(1, 'ceo',  '董事长',    1, '0', 'admin', sysdate(), '', null, '');
insert into system_post values(2, 'se',   '项目经理',  2, '0', 'admin', sysdate(), '', null, '');
insert into system_post values(3, 'hr',   '人力资源',  3, '0', 'admin', sysdate(), '', null, '');
insert into system_post values(4, 'user', '普通员工',  4, '0', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists system_role;
create table system_role (
                          role_id              bigint(20)      not null auto_increment    comment '角色ID',
                          role_name            varchar(30)     not null                   comment '角色名称',
                          role_key             varchar(100)    not null                   comment '角色权限字符串',
                          role_sort            int(4)          not null                   comment '显示顺序',
                          data_scope           char(1)         default '1'                comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
                          menu_check_strictly  tinyint(1)      default 1                  comment '菜单树选择项是否关联显示',
                          dept_check_strictly  tinyint(1)      default 1                  comment '部门树选择项是否关联显示',
                          status               char(1)         not null                   comment '角色状态（0正常 1停用）',
                          del_flag             char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
                          create_by            varchar(64)     default ''                 comment '创建者',
                          create_time          datetime                                   comment '创建时间',
                          update_by            varchar(64)     default ''                 comment '更新者',
                          update_time          datetime                                   comment '更新时间',
                          remark               varchar(500)    default null               comment '备注',
                          primary key (role_id)
) engine=innodb auto_increment=100 comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into system_role values('1', '超级管理员',  'admin',  1, 1, 1, 1, '0', '0', 'admin', sysdate(), '', null, '超级管理员');
insert into system_role values('2', '普通角色',    'common', 2, 2, 1, 1, '0', '0', 'admin', sysdate(), '', null, '普通角色');


-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists system_menu;
create table system_menu (
                          menu_id           bigint(20)      not null auto_increment    comment '菜单ID',
                          menu_name         varchar(50)     not null                   comment '菜单名称',
                          parent_id         bigint(20)      default 0                  comment '父菜单ID',
                          order_num         int(4)          default 0                  comment '显示顺序',
                          path              varchar(200)    default ''                 comment '路由地址',
                          component         varchar(255)    default null               comment '组件路径',
                          query             varchar(255)    default null               comment '路由参数',
                          route_name        varchar(50)     default ''                 comment '路由名称',
                          is_frame          int(1)          default 1                  comment '是否为外链（0是 1否）',
                          is_cache          int(1)          default 0                  comment '是否缓存（0缓存 1不缓存）',
                          menu_type         char(1)         default ''                 comment '菜单类型（M目录 C菜单 F按钮）',
                          visible           char(1)         default 0                  comment '菜单状态（0显示 1隐藏）',
                          status            char(1)         default 0                  comment '菜单状态（0正常 1停用）',
                          perms             varchar(100)    default null               comment '权限标识',
                          icon              varchar(100)    default '#'                comment '菜单图标',
                          create_by         varchar(64)     default ''                 comment '创建者',
                          create_time       datetime                                   comment '创建时间',
                          update_by         varchar(64)     default ''                 comment '更新者',
                          update_time       datetime                                   comment '更新时间',
                          remark            varchar(500)    default ''                 comment '备注',
                          primary key (menu_id)
) engine=innodb auto_increment=2000 comment = '菜单权限表';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
insert into system_menu values('1', '系统管理', '0', '5', 'system',           null, '', '', 1, 0, 'M', '0', '0', '', 'system',   'admin', sysdate(), '', null, '系统管理目录');
insert into system_menu values('2', '系统监控', '0', '6', 'monitor',          null, '', '', 1, 0, 'M', '0', '0', '', 'monitor',  'admin', sysdate(), '', null, '系统监控目录');
insert into system_menu values('3', '系统工具', '0', '7', 'tool',             null, '', '', 1, 0, 'M', '0', '0', '', 'tool',     'admin', sysdate(), '', null, '系统工具目录');
-- 二级菜单
insert into system_menu values('100',  '用户管理', '1',   '1', 'user',       'system/system/user/index',        '', '', 1, 0, 'C', '0', '0', 'system:user:list',        'user',          'admin', sysdate(), '', null, '用户管理菜单');
insert into system_menu values('101',  '角色管理', '1',   '2', 'role',       'system/system/role/index',        '', '', 1, 0, 'C', '0', '0', 'system:role:list',        'peoples',       'admin', sysdate(), '', null, '角色管理菜单');
insert into system_menu values('102',  '菜单管理', '1',   '3', 'menu',       'system/system/menu/index',        '', '', 1, 0, 'C', '0', '0', 'system:menu:list',        'tree-table',    'admin', sysdate(), '', null, '菜单管理菜单');
insert into system_menu values('103',  '部门管理', '1',   '4', 'dept',       'system/system/dept/index',        '', '', 1, 0, 'C', '0', '0', 'system:dept:list',        'tree',          'admin', sysdate(), '', null, '部门管理菜单');
insert into system_menu values('104',  '岗位管理', '1',   '5', 'post',       'system/system/post/index',        '', '', 1, 0, 'C', '0', '0', 'system:post:list',        'post',          'admin', sysdate(), '', null, '岗位管理菜单');
insert into system_menu values('105',  '字典管理', '1',   '6', 'dict',       'system/system/dict/index',        '', '', 1, 0, 'C', '0', '0', 'system:dict:list',        'dict',          'admin', sysdate(), '', null, '字典管理菜单');
insert into system_menu values('106',  '参数设置', '1',   '7', 'config',     'system/system/config/index',      '', '', 1, 0, 'C', '0', '0', 'system:config:list',      'edit',          'admin', sysdate(), '', null, '参数设置菜单');
insert into system_menu values('107',  '通知公告', '1',   '8', 'notice',     'system/system/notice/index',      '', '', 1, 0, 'C', '0', '0', 'system:notice:list',      'message',       'admin', sysdate(), '', null, '通知公告菜单');
insert into system_menu values('108',  '日志管理', '1',   '9', 'log',        '',                         '', '', 1, 0, 'M', '0', '0', '',                        'log',           'admin', sysdate(), '', null, '日志管理菜单');
insert into system_menu values('109',  '在线用户', '2',   '1', 'online',     'system/monitor/online/index',     '', '', 1, 0, 'C', '0', '0', 'monitor:online:list',     'online',        'admin', sysdate(), '', null, '在线用户菜单');
insert into system_menu values('110',  '定时任务', '2',   '2', 'job',        'system/monitor/job/index',        '', '', 1, 0, 'C', '0', '0', 'monitor:job:list',        'job',           'admin', sysdate(), '', null, '定时任务菜单');
insert into system_menu values('111',  '数据监控', '2',   '3', 'druid',      'system/monitor/druid/index',      '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list',      'druid',         'admin', sysdate(), '', null, '数据监控菜单');
insert into system_menu values('112',  '服务监控', '2',   '4', 'server',     'system/monitor/server/index',     '', '', 1, 0, 'C', '0', '0', 'monitor:server:list',     'server',        'admin', sysdate(), '', null, '服务监控菜单');
insert into system_menu values('113',  '缓存监控', '2',   '5', 'cache',      'system/monitor/cache/index',      '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list',      'redis',         'admin', sysdate(), '', null, '缓存监控菜单');
insert into system_menu values('114',  '缓存列表', '2',   '6', 'cacheList',  'system/monitor/cache/list',       '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list',      'redis-list',    'admin', sysdate(), '', null, '缓存列表菜单');
insert into system_menu values('115',  '表单构建', '3',   '1', 'build',      'system/tool/build/index',         '', '', 1, 0, 'C', '0', '0', 'tool:build:list',         'build',         'admin', sysdate(), '', null, '表单构建菜单');
insert into system_menu values('116',  '代码生成', '3',   '2', 'gen',        'system/tool/gen/index',           '', '', 1, 0, 'C', '0', '0', 'tool:gen:list',           'code',          'admin', sysdate(), '', null, '代码生成菜单');
insert into system_menu values('117',  '系统接口', '3',   '3', 'swagger',    'system/tool/swagger/index',       '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list',       'swagger',       'admin', sysdate(), '', null, '系统接口菜单');
-- 三级菜单
insert into system_menu values('500',  '操作日志', '108', '1', 'operlog',    'system/monitor/operlog/index',    '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list',    'form',          'admin', sysdate(), '', null, '操作日志菜单');
insert into system_menu values('501',  '登录日志', '108', '2', 'logininfor', 'system/monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor',    'admin', sysdate(), '', null, '登录日志菜单');
-- 用户管理按钮
insert into system_menu values('1000', '用户查询', '100', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query',          '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1001', '用户新增', '100', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add',            '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1002', '用户修改', '100', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit',           '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1003', '用户删除', '100', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove',         '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1004', '用户导出', '100', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export',         '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1005', '用户导入', '100', '6',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import',         '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1006', '重置密码', '100', '7',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd',       '#', 'admin', sysdate(), '', null, '');
-- 角色管理按钮
insert into system_menu values('1007', '角色查询', '101', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query',          '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1008', '角色新增', '101', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add',            '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1009', '角色修改', '101', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit',           '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1010', '角色删除', '101', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove',         '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1011', '角色导出', '101', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export',         '#', 'admin', sysdate(), '', null, '');
-- 菜单管理按钮
insert into system_menu values('1012', '菜单查询', '102', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query',          '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1013', '菜单新增', '102', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add',            '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1014', '菜单修改', '102', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit',           '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1015', '菜单删除', '102', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove',         '#', 'admin', sysdate(), '', null, '');
-- 部门管理按钮
insert into system_menu values('1016', '部门查询', '103', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query',          '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1017', '部门新增', '103', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add',            '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1018', '部门修改', '103', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit',           '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1019', '部门删除', '103', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove',         '#', 'admin', sysdate(), '', null, '');
-- 岗位管理按钮
insert into system_menu values('1020', '岗位查询', '104', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query',          '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1021', '岗位新增', '104', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add',            '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1022', '岗位修改', '104', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit',           '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1023', '岗位删除', '104', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove',         '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1024', '岗位导出', '104', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export',         '#', 'admin', sysdate(), '', null, '');
-- 字典管理按钮
insert into system_menu values('1025', '字典查询', '105', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query',          '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1026', '字典新增', '105', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add',            '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1027', '字典修改', '105', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit',           '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1028', '字典删除', '105', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove',         '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1029', '字典导出', '105', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export',         '#', 'admin', sysdate(), '', null, '');
-- 参数设置按钮
insert into system_menu values('1030', '参数查询', '106', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query',        '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1031', '参数新增', '106', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add',          '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1032', '参数修改', '106', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit',         '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1033', '参数删除', '106', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove',       '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1034', '参数导出', '106', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export',       '#', 'admin', sysdate(), '', null, '');
-- 通知公告按钮
insert into system_menu values('1035', '公告查询', '107', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query',        '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1036', '公告新增', '107', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add',          '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1037', '公告修改', '107', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit',         '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1038', '公告删除', '107', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove',       '#', 'admin', sysdate(), '', null, '');
-- 操作日志按钮
insert into system_menu values('1039', '操作查询', '500', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query',      '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1040', '操作删除', '500', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove',     '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1041', '日志导出', '500', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export',     '#', 'admin', sysdate(), '', null, '');
-- 登录日志按钮
insert into system_menu values('1042', '登录查询', '501', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query',   '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1043', '登录删除', '501', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove',  '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1044', '日志导出', '501', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export',  '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1045', '账户解锁', '501', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock',  '#', 'admin', sysdate(), '', null, '');
-- 在线用户按钮
insert into system_menu values('1046', '在线查询', '109', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query',       '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1047', '批量强退', '109', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1048', '单条强退', '109', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', sysdate(), '', null, '');
-- 定时任务按钮
insert into system_menu values('1049', '任务查询', '110', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query',          '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1050', '任务新增', '110', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add',            '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1051', '任务修改', '110', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit',           '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1052', '任务删除', '110', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove',         '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1053', '状态修改', '110', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus',   '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1054', '任务导出', '110', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export',         '#', 'admin', sysdate(), '', null, '');
-- 代码生成按钮
insert into system_menu values('1055', '生成查询', '116', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query',             '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1056', '生成修改', '116', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit',              '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1057', '生成删除', '116', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove',            '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1058', '导入代码', '116', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import',            '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1059', '预览代码', '116', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview',           '#', 'admin', sysdate(), '', null, '');
insert into system_menu values('1060', '生成代码', '116', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code',              '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 业务数据
-- ----------------------------

-- 【目录】知识中心
INSERT INTO `system_menu`  VALUES (2000, '知识中心', 0, 1, 'kmc', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', '知识中心', 'admin', '2025-02-10 17:21:49', 'admin', '2025-03-20 17:22:26', '');

-- 知识分类
INSERT INTO `system_menu`  VALUES (2001, '知识分类', 2000, 0, 'kmcCategory', 'kmc/kmcCategory/index', '', NULL, 1, 0, 'C', '0', '0', 'kmc:kmcCategory:kmcCategory:list', '#', 'admin', '2025-02-10 17:27:23', 'admin', '2025-02-18 17:16:52', '');
INSERT INTO `system_menu`  VALUES (2002, '知识分类导出', 2001, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:export', '#', 'admin', '2025-02-17 10:21:27', 'admin', '2025-02-18 17:21:11', '');
INSERT INTO `system_menu`  VALUES (2003, '知识分类导入', 2001, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:import', '#', 'admin', '2025-02-17 10:21:39', 'admin', '2025-02-18 17:20:46', '');
INSERT INTO `system_menu`  VALUES (2004, '知识分类详情', 2001, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:query', '#', 'admin', '2025-02-17 10:21:52', 'admin', '2025-02-18 17:20:54', '');
INSERT INTO `system_menu`  VALUES (2005, '知识分类新增', 2001, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:add', '#', 'admin', '2025-02-17 10:22:03', 'admin', '2025-02-18 17:20:59', '');
INSERT INTO `system_menu`  VALUES (2006, '知识分类修改', 2001, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:edit', '#', 'admin', '2025-02-17 10:22:12', 'admin', '2025-02-18 17:21:03', '');
INSERT INTO `system_menu`  VALUES (2007, '知识分类删除', 2001, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:remove', '#', 'admin', '2025-02-17 10:22:24', 'admin', '2025-02-18 17:21:07', '');

-- 文件管理
INSERT INTO `system_menu`  VALUES (2008, '文件管理', 2000, 1, 'kmcDocument', 'kmc/kmcDocument/index', '', NULL, 1, 0, 'C', '0', '0', 'kmcDocument:kmcDocument:document:list', '#', 'admin', '2025-02-10 17:27:09', 'admin', '2025-02-18 17:17:11', '');
INSERT INTO `system_menu`  VALUES (2009, '知识文件导出', 2008, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:export', '#', 'admin', '2025-02-17 10:27:10', 'admin', '2025-02-18 17:17:27', '');
INSERT INTO `system_menu`  VALUES (2010, '知识文件导入', 2008, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:import', '#', 'admin', '2025-02-17 10:27:21', 'admin', '2025-02-18 17:17:32', '');
INSERT INTO `system_menu`  VALUES (2011, '知识文件详情', 2008, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:query', '#', 'admin', '2025-02-17 10:27:30', 'admin', '2025-02-18 17:17:42', '');
INSERT INTO `system_menu`  VALUES (2012, '知识文件新增', 2008, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:add', '#', 'admin', '2025-02-17 10:27:40', 'admin', '2025-02-18 17:17:47', '');
INSERT INTO `system_menu`  VALUES (2013, '知识文件修改', 2008, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:edit', '#', 'admin', '2025-02-17 10:27:53', 'admin', '2025-02-18 17:17:52', '');
INSERT INTO `system_menu`  VALUES (2014, '知识文件删除', 2008, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:remove', '#', 'admin', '2025-02-17 10:28:08', 'admin', '2025-02-18 17:17:57', '');

-- 【目录】知识抽取
INSERT INTO `system_menu`  VALUES (2015, '知识抽取', 0, 2, 'ext', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', '知识抽取', 'admin', '2025-02-13 10:39:17', 'admin', '2025-03-20 17:25:23', '');

-- 概念配置
INSERT INTO `system_menu`  VALUES (2016, '概念配置', 2015, 1, 'schema', 'ext/extSchema/index', NULL, NULL, 1, 0, 'C', '0', '0', 'ext:extSchema:schema:list', '#', 'admin', '2025-02-17 16:03:40', 'admin', '2025-04-02 11:16:26', '');
INSERT INTO `system_menu`  VALUES (2017, '概念配置查询', 2016, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:query', '#', 'admin', '2025-02-18 17:24:07', 'admin', '2025-02-18 17:25:42', '');
INSERT INTO `system_menu`  VALUES (2018, '概念配置新增', 2016, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:add', '#', 'admin', '2025-02-18 17:24:24', 'admin', '2025-02-18 17:25:58', '');
INSERT INTO `system_menu`  VALUES (2019, '概念配置修改', 2016, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:edit', '#', 'admin', '2025-02-18 17:24:38', 'admin', '2025-02-18 17:26:03', '');
INSERT INTO `system_menu`  VALUES (2020, '概念配置删除', 2016, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:remove', '#', 'admin', '2025-02-18 17:25:01', 'admin', '2025-02-18 17:26:06', '');
INSERT INTO `system_menu`  VALUES (2021, '概念配置导出', 2016, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:export', '#', 'admin', '2025-02-18 17:25:16', 'admin', '2025-02-18 17:26:10', '');
INSERT INTO `system_menu`  VALUES (2022, '概念配置导入', 2016, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:import', '#', 'admin', '2025-02-18 17:25:30', 'admin', '2025-02-18 17:26:14', '');

-- 关系配置
INSERT INTO `system_menu`  VALUES (2023, '关系配置', 2015, 2, 'relation', 'ext/extSchemaRelation/index', NULL, NULL, 1, 0, 'C', '0', '0', 'ext:extSchemaRelation:relation:list', '#', 'admin', '2025-02-18 17:30:54', 'admin', '2025-04-02 11:16:31', '关系配置菜单');
INSERT INTO `system_menu`  VALUES (2024, '关系配置查询', 2023, 1, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:query', '#', 'admin', '2025-02-18 17:30:54', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2025, '关系配置新增', 2023, 2, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:add', '#', 'admin', '2025-02-18 17:30:54', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2026, '关系配置修改', 2023, 3, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:edit', '#', 'admin', '2025-02-18 17:30:54', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2027, '关系配置删除', 2023, 4, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:remove', '#', 'admin', '2025-02-18 17:30:54', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2028, '关系配置导出', 2023, 5, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:export', '#', 'admin', '2025-02-18 17:30:54', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2029, '关系配置导入', 2023, 6, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:import', '#', 'admin', '2025-02-18 17:30:54', '', NULL, '');

-- 非结构化抽取
INSERT INTO `system_menu`  VALUES (2030, '非结构化抽取', 2015, 3, 'unstructTask', 'ext/extUnstructTask/index', NULL, NULL, 1, 0, 'C', '0', '0', 'ext:extUnstructTask:unstructtask:list', '#', 'admin', '2025-02-18 16:49:34', 'admin', '2025-04-02 11:16:40', '非结构化抽取任务菜单');
INSERT INTO `system_menu`  VALUES (2031, '非结构化抽取任务查询', 2030, 1, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:query', '#', 'admin', '2025-02-18 16:49:34', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2032, '非结构化抽取任务新增', 2030, 2, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:add', '#', 'admin', '2025-02-18 16:49:34', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2033, '非结构化抽取任务修改', 2030, 3, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:edit', '#', 'admin', '2025-02-18 16:49:34', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2034, '非结构化抽取任务删除', 2030, 4, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:remove', '#', 'admin', '2025-02-18 16:49:34', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2035, '非结构化抽取任务导出', 2030, 5, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:export', '#', 'admin', '2025-02-18 16:49:34', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2036, '非结构化抽取任务导入', 2030, 6, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:import', '#', 'admin', '2025-02-18 16:49:34', '', NULL, '');

-- 结构化抽取
INSERT INTO `system_menu`  VALUES (2037, '结构化抽取', 2015, 4, 'extStructTask', 'ext/extStructTask/index', NULL, NULL, 1, 0, 'C', '0', '0', 'ext:extStructTask:struct:list', '#', 'admin', '2025-02-25 14:13:57', 'admin', '2025-04-02 11:16:44', '结构化抽取任务菜单');
INSERT INTO `system_menu`  VALUES (2038, '结构化抽取任务查询', 2037, 1, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:query', '#', 'admin', '2025-02-25 14:13:57', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2039, '结构化抽取任务新增', 2037, 2, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:add', '#', 'admin', '2025-02-25 14:13:57', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2040, '结构化抽取任务修改', 2037, 3, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:edit', '#', 'admin', '2025-02-25 14:13:57', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2041, '结构化抽取任务删除', 2037, 4, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:remove', '#', 'admin', '2025-02-25 14:13:57', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2042, '结构化抽取任务导出', 2037, 5, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:export', '#', 'admin', '2025-02-25 14:13:57', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2043, '结构化抽取任务导入', 2037, 6, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:import', '#', 'admin', '2025-02-25 14:13:57', '', NULL, '');

-- 【目录】知识应用
INSERT INTO `system_menu`  VALUES (2044, '知识应用', 0, 3, 'app', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', '知识应用', 'admin', '2025-02-17 16:25:46', 'admin', '2025-04-02 10:50:06', '');

-- 图谱探索
INSERT INTO `system_menu`  VALUES (2045, '图谱探索', 2044, 0, 'graphExploration', 'app/graphExploration/index', NULL, NULL, 1, 0, 'C', '0', '0', NULL, '#', 'admin', '2025-02-24 13:56:10', '', NULL, '');

-- 【目录】数据管理
INSERT INTO `system_menu`  VALUES (2046, '数据管理', 0, 4, 'dm', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', 'component', 'admin', '2025-02-19 15:15:49', 'admin', '2025-03-26 14:00:25', '');

-- 数据源
INSERT INTO `system_menu`  VALUES (2047, '数据源', 2046, 4, 'dmDatasource', 'dm/dmDatasource/index', NULL, NULL, 1, 0, 'C', '0', '0', 'dm:datasource:datasource:list', '#', 'admin', '2025-03-12 09:33:51', 'admin', '2025-03-18 17:56:57', '');
INSERT INTO `system_menu`  VALUES (2048, '数据源查询', 2047, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:query', '#', 'admin', '2025-03-18 17:18:11', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2049, '数据源新增', 2047, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:add', '#', 'admin', '2025-03-18 17:18:25', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2050, '数据源修改', 2047, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:edit', '#', 'admin', '2025-03-18 17:18:37', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2051, '数据源删除', 2047, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:remove', '#', 'admin', '2025-03-18 17:18:57', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2052, '数据源导出', 2047, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:export', '#', 'admin', '2025-03-18 17:19:11', '', NULL, '');
INSERT INTO `system_menu`  VALUES (2053, '数据源导入', 2047, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:import', '#', 'admin', '2025-03-18 17:19:23', '', NULL, '');

-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists system_user_role;
create table system_user_role (
                               user_id   bigint(20) not null comment '用户ID',
                               role_id   bigint(20) not null comment '角色ID',
                               primary key(user_id, role_id)
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into system_user_role values ('1', '1');


-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists system_role_menu;
create table system_role_menu (
                               role_id   bigint(20) not null comment '角色ID',
                               menu_id   bigint(20) not null comment '菜单ID',
                               primary key(role_id, menu_id)
) engine=innodb comment = '角色和菜单关联表';

-- ----------------------------
-- 8、角色和部门关联表  角色1-N部门
-- ----------------------------
drop table if exists system_role_dept;
create table system_role_dept (
                               role_id   bigint(20) not null comment '角色ID',
                               dept_id   bigint(20) not null comment '部门ID',
                               primary key(role_id, dept_id)
) engine=innodb comment = '角色和部门关联表';

-- ----------------------------
-- 9、用户与岗位关联表  用户1-N岗位
-- ----------------------------
drop table if exists system_user_post;
create table system_user_post
(
    user_id   bigint(20) not null comment '用户ID',
    post_id   bigint(20) not null comment '岗位ID',
    primary key (user_id, post_id)
) engine=innodb comment = '用户与岗位关联表';

-- ----------------------------
-- 初始化-用户与岗位关联表数据
-- ----------------------------
insert into system_user_post values ('1', '1');

-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
drop table if exists system_oper_log;
create table system_oper_log (
                              oper_id           bigint(20)      not null auto_increment    comment '日志主键',
                              title             varchar(50)     default ''                 comment '模块标题',
                              business_type     int(2)          default 0                  comment '业务类型（0其它 1新增 2修改 3删除）',
                              method            varchar(200)    default ''                 comment '方法名称',
                              request_method    varchar(10)     default ''                 comment '请求方式',
                              operator_type     int(1)          default 0                  comment '操作类别（0其它 1后台用户 2手机端用户）',
                              oper_name         varchar(50)     default ''                 comment '操作人员',
                              dept_name         varchar(50)     default ''                 comment '部门名称',
                              oper_url          varchar(255)    default ''                 comment '请求URL',
                              oper_ip           varchar(128)    default ''                 comment '主机地址',
                              oper_location     varchar(255)    default ''                 comment '操作地点',
                              oper_param        varchar(2000)   default ''                 comment '请求参数',
                              json_result       varchar(2000)   default ''                 comment '返回参数',
                              status            int(1)          default 0                  comment '操作状态（0正常 1异常）',
                              error_msg         varchar(2000)   default ''                 comment '错误消息',
                              oper_time         datetime                                   comment '操作时间',
                              cost_time         bigint(20)      default 0                  comment '消耗时间',
                              primary key (oper_id),
                              key idx_system_oper_log_bt (business_type),
                              key idx_system_oper_log_s  (status),
                              key idx_system_oper_log_ot (oper_time)
) engine=innodb auto_increment=1 comment = '操作日志记录';


-- ----------------------------
-- 11、字典类型表
-- ----------------------------
drop table if exists system_dict_type;
create table system_dict_type
(
    dict_id          bigint(20)      not null auto_increment    comment '字典主键',
    dict_name        varchar(100)    default ''                 comment '字典名称',
    dict_type        varchar(100)    default ''                 comment '字典类型',
    status           char(1)         default '0'                comment '状态（0正常 1停用）',
    create_by        varchar(64)     default ''                 comment '创建者',
    create_time      datetime                                   comment '创建时间',
    update_by        varchar(64)     default ''                 comment '更新者',
    update_time      datetime                                   comment '更新时间',
    remark           varchar(500)    default null               comment '备注',
    primary key (dict_id),
    unique (dict_type)
) engine=innodb auto_increment=100 comment = '字典类型表';

insert into system_dict_type values(1,  '用户性别', 'sys_user_sex',        '0', 'admin', sysdate(), '', null, '用户性别列表');
insert into system_dict_type values(2,  '菜单状态', 'sys_show_hide',       '0', 'admin', sysdate(), '', null, '菜单状态列表');
insert into system_dict_type values(3,  '系统开关', 'sys_normal_disable',  '0', 'admin', sysdate(), '', null, '系统开关列表');
insert into system_dict_type values(4,  '任务状态', 'sys_job_status',      '0', 'admin', sysdate(), '', null, '任务状态列表');
insert into system_dict_type values(5,  '任务分组', 'sys_job_group',       '0', 'admin', sysdate(), '', null, '任务分组列表');
insert into system_dict_type values(6,  '系统是否', 'sys_yes_no',          '0', 'admin', sysdate(), '', null, '系统是否列表');
insert into system_dict_type values(7,  '通知类型', 'sys_notice_type',     '0', 'admin', sysdate(), '', null, '通知类型列表');
insert into system_dict_type values(8,  '通知状态', 'sys_notice_status',   '0', 'admin', sysdate(), '', null, '通知状态列表');
insert into system_dict_type values(9,  '操作类型', 'sys_oper_type',       '0', 'admin', sysdate(), '', null, '操作类型列表');
insert into system_dict_type values(10, '系统状态', 'sys_common_status',   '0', 'admin', sysdate(), '', null, '登录状态列表');

INSERT INTO system_dict_type values(11, '数据类型', 'ext_data_type', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_type values(12, '数据校验', 'ext_data_check', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_type values(13, '发布状态', 'publish_status', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_type values(14, '任务执行状态', 'ext_task_status', '0', 'admin', sysdate(), '', NULL, NULL);

-- ----------------------------
-- 12、字典数据表
-- ----------------------------
drop table if exists system_dict_data;
create table system_dict_data
(
    dict_code        bigint(20)      not null auto_increment    comment '字典编码',
    dict_sort        int(4)          default 0                  comment '字典排序',
    dict_label       varchar(100)    default ''                 comment '字典标签',
    dict_value       varchar(100)    default ''                 comment '字典键值',
    dict_type        varchar(100)    default ''                 comment '字典类型',
    css_class        varchar(100)    default null               comment '样式属性（其他样式扩展）',
    list_class       varchar(100)    default null               comment '表格回显样式',
    is_default       char(1)         default 'N'                comment '是否默认（Y是 N否）',
    status           char(1)         default '0'                comment '状态（0正常 1停用）',
    create_by        varchar(64)     default ''                 comment '创建者',
    create_time      datetime                                   comment '创建时间',
    update_by        varchar(64)     default ''                 comment '更新者',
    update_time      datetime                                   comment '更新时间',
    remark           varchar(500)    default null               comment '备注',
    primary key (dict_code)
) engine=innodb auto_increment=100 comment = '字典数据表';

insert into system_dict_data values(1,  1,  '男',       '0',       'sys_user_sex',        '',   '',        'Y', '0', 'admin', sysdate(), '', null, '性别男');
insert into system_dict_data values(2,  2,  '女',       '1',       'sys_user_sex',        '',   '',        'N', '0', 'admin', sysdate(), '', null, '性别女');
insert into system_dict_data values(3,  3,  '未知',     '2',       'sys_user_sex',        '',   '',        'N', '0', 'admin', sysdate(), '', null, '性别未知');
insert into system_dict_data values(4,  1,  '显示',     '0',       'sys_show_hide',       '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '显示菜单');
insert into system_dict_data values(5,  2,  '隐藏',     '1',       'sys_show_hide',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '隐藏菜单');
insert into system_dict_data values(6,  1,  '正常',     '0',       'sys_normal_disable',  '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into system_dict_data values(7,  2,  '停用',     '1',       'sys_normal_disable',  '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');
insert into system_dict_data values(8,  1,  '正常',     '0',       'sys_job_status',      '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into system_dict_data values(9,  2,  '暂停',     '1',       'sys_job_status',      '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');
insert into system_dict_data values(10, 1,  '默认',     'DEFAULT', 'sys_job_group',       '',   '',        'Y', '0', 'admin', sysdate(), '', null, '默认分组');
insert into system_dict_data values(11, 2,  '系统',     'SYSTEM',  'sys_job_group',       '',   '',        'N', '0', 'admin', sysdate(), '', null, '系统分组');
insert into system_dict_data values(12, 1,  '是',       'Y',       'sys_yes_no',          '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '系统默认是');
insert into system_dict_data values(13, 2,  '否',       'N',       'sys_yes_no',          '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '系统默认否');
insert into system_dict_data values(14, 1,  '通知',     '1',       'sys_notice_type',     '',   'warning', 'Y', '0', 'admin', sysdate(), '', null, '通知');
insert into system_dict_data values(15, 2,  '公告',     '2',       'sys_notice_type',     '',   'success', 'N', '0', 'admin', sysdate(), '', null, '公告');
insert into system_dict_data values(16, 1,  '正常',     '0',       'sys_notice_status',   '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into system_dict_data values(17, 2,  '关闭',     '1',       'sys_notice_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '关闭状态');
insert into system_dict_data values(18, 99, '其他',     '0',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '其他操作');
insert into system_dict_data values(19, 1,  '新增',     '1',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '新增操作');
insert into system_dict_data values(20, 2,  '修改',     '2',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '修改操作');
insert into system_dict_data values(21, 3,  '删除',     '3',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '删除操作');
insert into system_dict_data values(22, 4,  '授权',     '4',       'sys_oper_type',       '',   'primary', 'N', '0', 'admin', sysdate(), '', null, '授权操作');
insert into system_dict_data values(23, 5,  '导出',     '5',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '导出操作');
insert into system_dict_data values(24, 6,  '导入',     '6',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '导入操作');
insert into system_dict_data values(25, 7,  '强退',     '7',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '强退操作');
insert into system_dict_data values(26, 8,  '生成代码', '8',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '生成操作');
insert into system_dict_data values(27, 9,  '清空数据', '9',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '清空操作');
insert into system_dict_data values(28, 1,  '成功',     '0',       'sys_common_status',   '',   'primary', 'N', '0', 'admin', sysdate(), '', null, '正常状态');
insert into system_dict_data values(29, 2,  '失败',     '1',       'sys_common_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');

INSERT INTO system_dict_data values(30, 0, '文本', '0', 'ext_data_type', NULL, 'default', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(31, 1, '整数', '1', 'ext_data_type', NULL, 'default', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(32, 2, '小数', '2', 'ext_data_type', NULL, 'default', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(33, 3, '时间', '3', 'ext_data_type', NULL, 'default', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(34, 4, '字节类型', '4', 'ext_data_type', NULL, 'default', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(35, 5, '布尔值', '5', 'ext_data_type', NULL, 'default', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(36, 0, '唯一性校验', '0', 'ext_data_check', NULL, 'default', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(37, 1, '长度校验', '1', 'ext_data_check', NULL, 'default', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(38, 2, '区间校验', '2', 'ext_data_check', NULL, 'default', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(39, 0, '未发布', '0', 'publish_status', NULL, 'warning', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(40, 1, '已发布', '1', 'publish_status', NULL, 'success', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(41, 0, '未执行', '0', 'ext_task_status', NULL, 'primary', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(42, 1, '进行中', '1', 'ext_task_status', NULL, 'warning', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(43, 2, '已完成', '2', 'ext_task_status', NULL, 'success', 'N', '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data values(44, 3, '执行失败', '3', 'ext_task_status', NULL, 'danger', 'N', '0', 'admin', sysdate(), '', NULL, NULL);

-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists system_config;
create table system_config (
                            config_id         int(5)          not null auto_increment    comment '参数主键',
                            config_name       varchar(100)    default ''                 comment '参数名称',
                            config_key        varchar(100)    default ''                 comment '参数键名',
                            config_value      varchar(500)    default ''                 comment '参数键值',
                            config_type       char(1)         default 'N'                comment '系统内置（Y是 N否）',
                            create_by         varchar(64)     default ''                 comment '创建者',
                            create_time       datetime                                   comment '创建时间',
                            update_by         varchar(64)     default ''                 comment '更新者',
                            update_time       datetime                                   comment '更新时间',
                            remark            varchar(500)    default null               comment '备注',
                            primary key (config_id)
) engine=innodb auto_increment=100 comment = '参数配置表';

insert into system_config values(1, '主框架页-默认皮肤样式名称',     'sys.index.skinName',            'skin-blue',     'Y', 'admin', sysdate(), '', null, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow' );
insert into system_config values(2, '用户管理-账号初始密码',         'sys.user.initPassword',         '123456',        'Y', 'admin', sysdate(), '', null, '初始化密码 123456' );
insert into system_config values(3, '主框架页-侧边栏主题',           'sys.index.sideTheme',           'theme-dark',    'Y', 'admin', sysdate(), '', null, '深色主题theme-dark，浅色主题theme-light' );
insert into system_config values(4, '账号自助-验证码开关',           'sys.account.captchaEnabled',    'true',          'Y', 'admin', sysdate(), '', null, '是否开启验证码功能（true开启，false关闭）');
insert into system_config values(5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser',      'false',         'Y', 'admin', sysdate(), '', null, '是否开启注册用户功能（true开启，false关闭）');
insert into system_config values(6, '用户登录-黑名单列表',           'sys.login.blackIPList',         '',              'Y', 'admin', sysdate(), '', null, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');


-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
drop table if exists system_logininfor;
create table system_logininfor (
                                info_id        bigint(20)     not null auto_increment   comment '访问ID',
                                user_name      varchar(50)    default ''                comment '用户账号',
                                ipaddr         varchar(128)   default ''                comment '登录IP地址',
                                login_location varchar(255)   default ''                comment '登录地点',
                                browser        varchar(50)    default ''                comment '浏览器类型',
                                os             varchar(50)    default ''                comment '操作系统',
                                status         char(1)        default '0'               comment '登录状态（0成功 1失败）',
                                msg            varchar(255)   default ''                comment '提示消息',
                                login_time     datetime                                 comment '访问时间',
                                primary key (info_id),
                                key idx_system_logininfor_s  (status),
                                key idx_system_logininfor_lt (login_time)
) engine=innodb auto_increment=100 comment = '系统访问记录';


-- ----------------------------
-- 15、定时任务调度表
-- ----------------------------
drop table if exists system_job;
create table system_job (
                         job_id              bigint(20)    not null auto_increment    comment '任务ID',
                         job_name            varchar(64)   default ''                 comment '任务名称',
                         job_group           varchar(64)   default 'DEFAULT'          comment '任务组名',
                         invoke_target       varchar(500)  not null                   comment '调用目标字符串',
                         cron_expression     varchar(255)  default ''                 comment 'cron执行表达式',
                         misfire_policy      varchar(20)   default '3'                comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
                         concurrent          char(1)       default '1'                comment '是否并发执行（0允许 1禁止）',
                         status              char(1)       default '0'                comment '状态（0正常 1暂停）',
                         create_by           varchar(64)   default ''                 comment '创建者',
                         create_time         datetime                                 comment '创建时间',
                         update_by           varchar(64)   default ''                 comment '更新者',
                         update_time         datetime                                 comment '更新时间',
                         remark              varchar(500)  default ''                 comment '备注信息',
                         primary key (job_id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';

insert into system_job values(1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams',        '0/10 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');
insert into system_job values(2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')',  '0/15 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');
insert into system_job values(3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)',  '0/20 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 16、定时任务调度日志表
-- ----------------------------
drop table if exists system_job_log;
create table system_job_log (
                             job_log_id          bigint(20)     not null auto_increment    comment '任务日志ID',
                             job_name            varchar(64)    not null                   comment '任务名称',
                             job_group           varchar(64)    not null                   comment '任务组名',
                             invoke_target       varchar(500)   not null                   comment '调用目标字符串',
                             job_message         varchar(500)                              comment '日志信息',
                             status              char(1)        default '0'                comment '执行状态（0正常 1失败）',
                             exception_info      varchar(2000)  default ''                 comment '异常信息',
                             create_time         datetime                                  comment '创建时间',
                             primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';


-- ----------------------------
-- 17、通知公告表
-- ----------------------------
drop table if exists system_notice;
create table system_notice (
                            notice_id         int(4)          not null auto_increment    comment '公告ID',
                            notice_title      varchar(50)     not null                   comment '公告标题',
                            notice_type       char(1)         not null                   comment '公告类型（1通知 2公告）',
                            notice_content    longblob        default null               comment '公告内容',
                            status            char(1)         default '0'                comment '公告状态（0正常 1关闭）',
                            create_by         varchar(64)     default ''                 comment '创建者',
                            create_time       datetime                                   comment '创建时间',
                            update_by         varchar(64)     default ''                 comment '更新者',
                            update_time       datetime                                   comment '更新时间',
                            remark            varchar(255)    default null               comment '备注',
                            primary key (notice_id)
) engine=innodb auto_increment=100 comment = '通知公告表';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------
insert into system_notice values('1', '温馨提醒：2025-05-20 千知平台新版本发布啦', '2', '新版本内容', '0', 'admin', sysdate(), '', null, '管理员');
insert into system_notice values('2', '维护通知：2025-05-22 千知平台凌晨维护', '1', '维护内容',   '0', 'admin', sysdate(), '', null, '管理员');


-- ----------------------------
-- 18、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
                           table_id          bigint(20)      not null auto_increment    comment '编号',
                           table_name        varchar(200)    default ''                 comment '表名称',
                           table_comment     varchar(500)    default ''                 comment '表描述',
                           sub_table_name    varchar(64)     default null               comment '关联子表的表名',
                           sub_table_fk_name varchar(64)     default null               comment '子表关联的外键名',
                           class_name        varchar(100)    default ''                 comment '实体类名称',
                           tpl_category      varchar(200)    default 'crud'             comment '使用的模板（crud单表操作 tree树表操作）',
                           tpl_web_type      varchar(30)     default ''                 comment '前端模板类型（element-ui模版 element-plus模版）',
                           package_name      varchar(100)                               comment '生成包路径',
                           module_name       varchar(30)                                comment '生成模块名',
                           business_name     varchar(30)                                comment '生成业务名',
                           function_name     varchar(50)                                comment '生成功能名',
                           function_author   varchar(50)                                comment '生成功能作者',
                           gen_type          char(1)         default '0'                comment '生成代码方式（0zip压缩包 1自定义路径）',
                           gen_path          varchar(200)    default '/'                comment '生成路径（不填默认项目路径）',
                           options           varchar(1000)                              comment '其它生成选项',
                           create_by         varchar(64)     default ''                 comment '创建者',
                           create_time 	    datetime                                   comment '创建时间',
                           update_by         varchar(64)     default ''                 comment '更新者',
                           update_time       datetime                                   comment '更新时间',
                           remark            varchar(500)    default null               comment '备注',
                           primary key (table_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';


-- ----------------------------
-- 19、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
                                  column_id         bigint(20)      not null auto_increment    comment '编号',
                                  table_id          bigint(20)                                 comment '归属表编号',
                                  column_name       varchar(200)                               comment '列名称',
                                  column_comment    varchar(500)                               comment '列描述',
                                  column_type       varchar(100)                               comment '列类型',
                                  java_type         varchar(500)                               comment 'JAVA类型',
                                  java_field        varchar(200)                               comment 'JAVA字段名',
                                  is_pk             char(1)                                    comment '是否主键（1是）',
                                  is_increment      char(1)                                    comment '是否自增（1是）',
                                  is_required       char(1)                                    comment '是否必填（1是）',
                                  is_insert         char(1)                                    comment '是否为插入字段（1是）',
                                  is_edit           char(1)                                    comment '是否编辑字段（1是）',
                                  is_list           char(1)                                    comment '是否列表字段（1是）',
                                  is_query          char(1)                                    comment '是否查询字段（1是）',
                                  query_type        varchar(200)    default 'EQ'               comment '查询方式（等于、不等于、大于、小于、范围）',
                                  html_type         varchar(200)                               comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
                                  dict_type         varchar(200)    default ''                 comment '字典类型',
                                  sort              int                                        comment '排序',
                                  create_by         varchar(64)     default ''                 comment '创建者',
                                  create_time 	    datetime                                   comment '创建时间',
                                  update_by         varchar(64)     default ''                 comment '更新者',
                                  update_time       datetime                                   comment '更新时间',
                                  primary key (column_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';

-- -------------------------------------------------------------------------------------------------------------
-- 额外补充
-- -------------------------------------------------------------------------------------------------------------

-- ----------------------------
-- Table structure for system_content
-- ----------------------------
DROP TABLE IF EXISTS `system_content`;
CREATE TABLE `system_content` (
                                  `id` int(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                  `sys_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '系统名称',
                                  `logo` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '系统logo',
                                  `login_logo` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录页面logo',
                                  `carousel_image` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '轮播图',
                                  `contact_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
                                  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电子邮箱',
                                  `copyright` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '版权方',
                                  `record_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备案号',
                                  `del_flag` int(2) DEFAULT NULL COMMENT '删除标记',
                                  `status` int(2) DEFAULT NULL COMMENT '状态',
                                  `create_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
                                  `creator_id` int(16) DEFAULT NULL COMMENT '创建人id',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `update_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
                                  `updater_id` int(16) DEFAULT NULL COMMENT '修改人id',
                                  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of system_content
-- ----------------------------
BEGIN;
INSERT INTO `system_content` (`id`, `sys_name`, `logo`, `login_logo`, `carousel_image`, `contact_number`, `email`, `copyright`, `record_number`, `del_flag`, `status`, `create_by`, `creator_id`, `create_time`, `update_by`, `updater_id`, `update_time`, `remark`) VALUES (1, NULL, '', '', '', '400-660-8208', 'support@qiantong.tech', 'Copyright© 2025 江苏干桐科技有限公司 版权所有', '苏ICP备2022008519号-3', 0, NULL, NULL, NULL, NULL, 'admin', 1, '2025-01-13 13:18:06', NULL);
COMMIT;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
                           `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                           `sender_id` bigint(20) DEFAULT NULL COMMENT '发送人',
                           `receiver_id` bigint(20) DEFAULT NULL COMMENT '接收人',
                           `title` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息标题',
                           `content` varchar(3072) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息模板内容',
                           `category` tinyint(4) unsigned NOT NULL COMMENT '消息类别',
                           `msg_level` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息等级',
                           `module` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息模块',
                           `entity_type` tinyint(4) unsigned DEFAULT NULL COMMENT '实体类型',
                           `entity_id` bigint(20) DEFAULT NULL COMMENT '实体id',
                           `entity_url` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息链接',
                           `has_read` tinyint(1) DEFAULT '0' COMMENT '是否已读',
                           `has_retraction` tinyint(1) DEFAULT '0' COMMENT '是否撤回',
                           `valid_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                           `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                           `create_by` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
                           `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_by` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                           `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                           `remark` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='消息';
