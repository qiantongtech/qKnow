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
insert into system_dept values(100,  0,   '0',          '千桐科技',   0, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', '小桐', sysdate(), '', null);
insert into system_dept values(101,  100, '0,100',      '南京总公司', 1, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', '小桐', sysdate(), '', null);
insert into system_dept values(102,  100, '0,100',      '郑州分公司', 2, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', '小桐', sysdate(), '', null);
insert into system_dept values(103,  101, '0,100,101',  '研发部门',   1, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', '小桐', sysdate(), '', null);
insert into system_dept values(104,  101, '0,100,101',  '市场部门',   2, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', '小桐', sysdate(), '', null);
insert into system_dept values(105,  101, '0,100,101',  '测试部门',   3, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', '小桐', sysdate(), '', null);
insert into system_dept values(106,  101, '0,100,101',  '财务部门',   4, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', '小桐', sysdate(), '', null);
insert into system_dept values(107,  101, '0,100,101',  '运维部门',   5, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', '小桐', sysdate(), '', null);
insert into system_dept values(108,  102, '0,100,102',  '市场部门',   1, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', '小桐', sysdate(), '', null);
insert into system_dept values(109,  102, '0,100,102',  '财务部门',   2, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', '小桐', sysdate(), '', null);


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
insert into system_user values(1,  103, 'qKnow', '小桐', '00', 'support@qiantong.tech', '15888888888', '0', '', '$2a$10$M9QTlVS3URMVLDMMmJYYress8MgeKE0ahcNQSwO.T/TI8/U1U7pF6', '0', '0', '127.0.0.1', sysdate(), '小桐', sysdate(), '', null, '管理员');


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
insert into system_post values(1, 'ceo',  '董事长',    1, '0', '小桐', sysdate(), '', null, '');
insert into system_post values(2, 'se',   '项目经理',  2, '0', '小桐', sysdate(), '', null, '');
insert into system_post values(3, 'hr',   '人力资源',  3, '0', '小桐', sysdate(), '', null, '');
insert into system_post values(4, 'user', '普通员工',  4, '0', '小桐', sysdate(), '', null, '');


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
insert into system_role values('1', '超级管理员',  'admin',  1, 1, 1, 1, '0', '0', '小桐', sysdate(), '', null, '超级管理员');
insert into system_role values('2', '普通角色',    'common', 2, 2, 1, 1, '0', '0', '小桐', sysdate(), '', null, '普通角色');


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
insert into system_menu values('1', '系统管理', '0', '5', 'system',           null, '', '', 1, 0, 'M', '0', '0', '', '系统设置',   '小桐', sysdate(), '', null, '系统管理目录');
insert into system_menu values('2', '系统监控', '0', '6', 'monitor',          null, '', '', 1, 0, 'M', '0', '0', '', '系统监控',  '小桐', sysdate(), '', null, '系统监控目录');
insert into system_menu values('3', '系统工具', '0', '7', 'tool',             null, '', '', 1, 0, 'M', '0', '0', '', '系统工具',     '小桐', sysdate(), '', null, '系统工具目录');
-- 二级菜单
insert into system_menu values('100',  '用户管理', '1',   '1', 'user',       'system/system/user/index',        '', '', 1, 0, 'C', '0', '0', 'system:user:list',        'user',          '小桐', sysdate(), '', null, '用户管理菜单');
insert into system_menu values('101',  '角色管理', '1',   '2', 'role',       'system/system/role/index',        '', '', 1, 0, 'C', '0', '0', 'system:role:list',        'peoples',       '小桐', sysdate(), '', null, '角色管理菜单');
insert into system_menu values('102',  '菜单管理', '1',   '3', 'menu',       'system/system/menu/index',        '', '', 1, 0, 'C', '0', '0', 'system:menu:list',        'tree-table',    '小桐', sysdate(), '', null, '菜单管理菜单');
insert into system_menu values('103',  '部门管理', '1',   '4', 'dept',       'system/system/dept/index',        '', '', 1, 0, 'C', '0', '0', 'system:dept:list',        'tree',          '小桐', sysdate(), '', null, '部门管理菜单');
insert into system_menu values('104',  '岗位管理', '1',   '5', 'post',       'system/system/post/index',        '', '', 1, 0, 'C', '0', '0', 'system:post:list',        'post',          '小桐', sysdate(), '', null, '岗位管理菜单');
insert into system_menu values('105',  '字典管理', '1',   '6', 'dict',       'system/system/dict/index',        '', '', 1, 0, 'C', '0', '0', 'system:dict:list',        'dict',          '小桐', sysdate(), '', null, '字典管理菜单');
insert into system_menu values('106',  '参数设置', '1',   '7', 'config',     'system/system/config/index',      '', '', 1, 0, 'C', '0', '0', 'system:config:list',      'edit',          '小桐', sysdate(), '', null, '参数设置菜单');
insert into system_menu values('107',  '通知公告', '1',   '8', 'notice',     'system/system/notice/index',      '', '', 1, 0, 'C', '0', '0', 'system:notice:list',      'message',       '小桐', sysdate(), '', null, '通知公告菜单');
insert into system_menu values('108',  '日志管理', '1',   '9', 'log',        '',                         '', '', 1, 0, 'M', '0', '0', '',                        'log',           '小桐', sysdate(), '', null, '日志管理菜单');
insert into system_menu values('109',  '在线用户', '2',   '1', 'online',     'system/monitor/online/index',     '', '', 1, 0, 'C', '0', '0', 'monitor:online:list',     'online',        '小桐', sysdate(), '', null, '在线用户菜单');
insert into system_menu values('110',  '定时任务', '2',   '2', 'job',        'system/monitor/job/index',        '', '', 1, 0, 'C', '0', '0', 'monitor:job:list',        'job',           '小桐', sysdate(), '', null, '定时任务菜单');
insert into system_menu values('111',  '服务监控', '2',   '4', 'server',     'system/monitor/server/index',     '', '', 1, 0, 'C', '0', '0', 'monitor:server:list',     'server',        '小桐', sysdate(), '', null, '服务监控菜单');
insert into system_menu values('112',  '缓存监控', '2',   '5', 'cache',      'system/monitor/cache/index',      '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list',      'redis',         '小桐', sysdate(), '', null, '缓存监控菜单');
insert into system_menu values('113',  '缓存列表', '2',   '6', 'cacheList',  'system/monitor/cache/list',       '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list',      'redis-list',    '小桐', sysdate(), '', null, '缓存列表菜单');
insert into system_menu values('114',  '代码生成', '3',   '2', 'gen',        'system/tool/gen/index',           '', '', 1, 0, 'C', '0', '0', 'tool:gen:list',           'code',          '小桐', sysdate(), '', null, '代码生成菜单');
insert into system_menu values('115',  '系统接口', '3',   '3', 'swagger',    'system/tool/swagger/index',       '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list',       'swagger',       '小桐', sysdate(), '', null, '系统接口菜单');
-- 三级菜单
insert into system_menu values('500',  '操作日志', '108', '1', 'operlog',    'system/monitor/operlog/index',    '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list',    'form',          '小桐', sysdate(), '', null, '操作日志菜单');
insert into system_menu values('501',  '登录日志', '108', '2', 'logininfor', 'system/monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor',    '小桐', sysdate(), '', null, '登录日志菜单');
-- 用户管理按钮
insert into system_menu values('1000', '用户查询', '100', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query',          '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1001', '用户新增', '100', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add',            '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1002', '用户修改', '100', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit',           '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1003', '用户删除', '100', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove',         '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1004', '用户导出', '100', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export',         '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1005', '用户导入', '100', '6',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import',         '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1006', '重置密码', '100', '7',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd',       '#', '小桐', sysdate(), '', null, '');
-- 角色管理按钮
insert into system_menu values('1007', '角色查询', '101', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query',          '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1008', '角色新增', '101', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add',            '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1009', '角色修改', '101', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit',           '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1010', '角色删除', '101', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove',         '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1011', '角色导出', '101', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export',         '#', '小桐', sysdate(), '', null, '');
-- 菜单管理按钮
insert into system_menu values('1012', '菜单查询', '102', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query',          '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1013', '菜单新增', '102', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add',            '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1014', '菜单修改', '102', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit',           '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1015', '菜单删除', '102', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove',         '#', '小桐', sysdate(), '', null, '');
-- 部门管理按钮
insert into system_menu values('1016', '部门查询', '103', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query',          '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1017', '部门新增', '103', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add',            '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1018', '部门修改', '103', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit',           '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1019', '部门删除', '103', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove',         '#', '小桐', sysdate(), '', null, '');
-- 岗位管理按钮
insert into system_menu values('1020', '岗位查询', '104', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query',          '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1021', '岗位新增', '104', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add',            '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1022', '岗位修改', '104', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit',           '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1023', '岗位删除', '104', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove',         '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1024', '岗位导出', '104', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export',         '#', '小桐', sysdate(), '', null, '');
-- 字典管理按钮
insert into system_menu values('1025', '字典查询', '105', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query',          '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1026', '字典新增', '105', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add',            '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1027', '字典修改', '105', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit',           '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1028', '字典删除', '105', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove',         '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1029', '字典导出', '105', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export',         '#', '小桐', sysdate(), '', null, '');
-- 参数设置按钮
insert into system_menu values('1030', '参数查询', '106', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query',        '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1031', '参数新增', '106', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add',          '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1032', '参数修改', '106', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit',         '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1033', '参数删除', '106', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove',       '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1034', '参数导出', '106', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export',       '#', '小桐', sysdate(), '', null, '');
-- 通知公告按钮
insert into system_menu values('1035', '公告查询', '107', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query',        '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1036', '公告新增', '107', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add',          '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1037', '公告修改', '107', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit',         '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1038', '公告删除', '107', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove',       '#', '小桐', sysdate(), '', null, '');
-- 操作日志按钮
insert into system_menu values('1039', '操作查询', '500', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query',      '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1040', '操作删除', '500', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove',     '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1041', '日志导出', '500', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export',     '#', '小桐', sysdate(), '', null, '');
-- 登录日志按钮
insert into system_menu values('1042', '登录查询', '501', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query',   '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1043', '登录删除', '501', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove',  '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1044', '日志导出', '501', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export',  '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1045', '账户解锁', '501', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock',  '#', '小桐', sysdate(), '', null, '');
-- 在线用户按钮
insert into system_menu values('1046', '在线查询', '109', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query',       '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1047', '批量强退', '109', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1048', '单条强退', '109', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', '小桐', sysdate(), '', null, '');
-- 定时任务按钮
insert into system_menu values('1049', '任务查询', '110', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query',          '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1050', '任务新增', '110', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add',            '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1051', '任务修改', '110', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit',           '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1052', '任务删除', '110', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove',         '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1053', '状态修改', '110', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus',   '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1054', '任务导出', '110', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export',         '#', '小桐', sysdate(), '', null, '');
-- 代码生成按钮
insert into system_menu values('1055', '生成查询', '114', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query',             '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1056', '生成修改', '114', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit',              '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1057', '生成删除', '114', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove',            '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1058', '导入代码', '114', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import',            '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1059', '预览代码', '114', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview',           '#', '小桐', sysdate(), '', null, '');
insert into system_menu values('1060', '生成代码', '114', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code',              '#', '小桐', sysdate(), '', null, '');

-- ----------------------------
-- 业务数据
-- ----------------------------

-- 【目录】知识中心
insert into system_menu  values (2000, '知识中心', 0, 1, 'kmc', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', '知识中心', '小桐', sysdate(), '', null, '');

-- 知识分类
insert into system_menu  values (2001, '知识分类', 2000, 0, 'kmcCategory', 'kmc/kmcCategory/index', '', NULL, 1, 0, 'C', '0', '0', 'kmc:kmcCategory:kmcCategory:list', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2002, '知识分类导出', 2001, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:export', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2003, '知识分类导入', 2001, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:import', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2004, '知识分类详情', 2001, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:query', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2005, '知识分类新增', 2001, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:add', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2006, '知识分类修改', 2001, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:edit', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2007, '知识分类删除', 2001, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:remove', '#', '小桐', sysdate(), '', null, '');

-- 文件管理
insert into system_menu  values (2008, '文件管理', 2000, 1, 'kmcDocument', 'kmc/kmcDocument/index', '', NULL, 1, 0, 'C', '0', '0', 'kmcDocument:kmcDocument:document:list', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2009, '知识文件导出', 2008, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:export', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2010, '知识文件导入', 2008, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:import', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2011, '知识文件详情', 2008, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:query', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2012, '知识文件新增', 2008, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:add', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2013, '知识文件修改', 2008, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:edit', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2014, '知识文件删除', 2008, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:remove', '#', '小桐', sysdate(), '', null, '');

-- 【目录】知识抽取
insert into system_menu  values (2015, '知识抽取', 0, 2, 'ext', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', '知识抽取', '小桐', sysdate(), '', null, '');

-- 概念配置
insert into system_menu  values (2016, '概念配置', 2015, 1, 'schema', 'ext/extSchema/index', NULL, NULL, 1, 0, 'C', '0', '0', 'ext:extSchema:schema:list', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2017, '概念配置查询', 2016, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:query', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2018, '概念配置新增', 2016, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:add', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2019, '概念配置修改', 2016, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:edit', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2020, '概念配置删除', 2016, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:remove', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2021, '概念配置导出', 2016, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:export', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2022, '概念配置导入', 2016, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:import', '#', '小桐', sysdate(), '', null, '');

-- 关系配置
insert into system_menu  values (2023, '关系配置', 2015, 2, 'relation', 'ext/extSchemaRelation/index', NULL, NULL, 1, 0, 'C', '0', '0', 'ext:extSchemaRelation:relation:list', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2024, '关系配置查询', 2023, 1, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:query', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2025, '关系配置新增', 2023, 2, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:add', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2026, '关系配置修改', 2023, 3, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:edit', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2027, '关系配置删除', 2023, 4, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:remove', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2028, '关系配置导出', 2023, 5, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:export', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2029, '关系配置导入', 2023, 6, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:import', '#', '小桐', sysdate(), '', null, '');

-- 非结构化抽取
insert into system_menu  values (2030, '非结构化抽取', 2015, 3, 'unstructTask', 'ext/extUnstructTask/index', NULL, NULL, 1, 0, 'C', '0', '0', 'ext:extUnstructTask:unstructtask:list', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2031, '非结构化抽取任务查询', 2030, 1, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:query', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2032, '非结构化抽取任务新增', 2030, 2, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:add', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2033, '非结构化抽取任务修改', 2030, 3, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:edit', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2034, '非结构化抽取任务删除', 2030, 4, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:remove', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2035, '非结构化抽取任务导出', 2030, 5, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:export', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2036, '非结构化抽取任务导入', 2030, 6, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:import', '#', '小桐', sysdate(), '', null, '');

-- 结构化抽取
insert into system_menu  values (2037, '结构化抽取', 2015, 4, 'extStructTask', 'ext/extStructTask/index', NULL, NULL, 1, 0, 'C', '0', '0', 'ext:extStructTask:struct:list', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2038, '结构化抽取任务查询', 2037, 1, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:query', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2039, '结构化抽取任务新增', 2037, 2, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:add', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2040, '结构化抽取任务修改', 2037, 3, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:edit', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2041, '结构化抽取任务删除', 2037, 4, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:remove', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2042, '结构化抽取任务导出', 2037, 5, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:export', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2043, '结构化抽取任务导入', 2037, 6, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:import', '#', '小桐', sysdate(), '', null, '');

-- 【目录】知识应用
insert into system_menu  values (2044, '知识应用', 0, 3, 'app', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', '知识应用', '小桐', sysdate(), '', null, '');

-- 图谱探索
insert into system_menu  values (2045, '图谱探索', 2044, 0, 'graphExploration', 'app/graphExploration/index', NULL, NULL, 1, 0, 'C', '0', '0', NULL, '#', '小桐', sysdate(), '', null, '');

-- 【目录】数据管理
insert into system_menu  values (2046, '数据管理', 0, 4, 'dm', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', '数据管理', '小桐', sysdate(), '', null, '');

-- 数据源
insert into system_menu  values (2047, '数据源', 2046, 4, 'dmDatasource', 'dm/dmDatasource/index', NULL, NULL, 1, 0, 'C', '0', '0', 'dm:datasource:datasource:list', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2048, '数据源查询', 2047, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:query', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2049, '数据源新增', 2047, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:add', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2050, '数据源修改', 2047, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:edit', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2051, '数据源删除', 2047, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:remove', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2052, '数据源导出', 2047, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:export', '#', '小桐', sysdate(), '', null, '');
insert into system_menu  values (2053, '数据源导入', 2047, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:import', '#', '小桐', sysdate(), '', null, '');

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

insert into system_dict_type values(1,  '用户性别', 'sys_user_sex',        '0', '小桐', sysdate(), '', null, '用户性别列表');
insert into system_dict_type values(2,  '菜单状态', 'sys_show_hide',       '0', '小桐', sysdate(), '', null, '菜单状态列表');
insert into system_dict_type values(3,  '系统开关', 'sys_normal_disable',  '0', '小桐', sysdate(), '', null, '系统开关列表');
insert into system_dict_type values(4,  '任务状态', 'sys_job_status',      '0', '小桐', sysdate(), '', null, '任务状态列表');
insert into system_dict_type values(5,  '任务分组', 'sys_job_group',       '0', '小桐', sysdate(), '', null, '任务分组列表');
insert into system_dict_type values(6,  '系统是否', 'sys_yes_no',          '0', '小桐', sysdate(), '', null, '系统是否列表');
insert into system_dict_type values(7,  '通知类型', 'sys_notice_type',     '0', '小桐', sysdate(), '', null, '通知类型列表');
insert into system_dict_type values(8,  '通知状态', 'sys_notice_status',   '0', '小桐', sysdate(), '', null, '通知状态列表');
insert into system_dict_type values(9,  '操作类型', 'sys_oper_type',       '0', '小桐', sysdate(), '', null, '操作类型列表');
insert into system_dict_type values(10, '系统状态', 'sys_common_status',   '0', '小桐', sysdate(), '', null, '登录状态列表');

insert into system_dict_type values(11, '数据类型', 'ext_data_type', '0', '小桐', sysdate(), '', NULL, '数据类型列表');
insert into system_dict_type values(12, '数据校验', 'ext_data_check', '0', '小桐', sysdate(), '', NULL, '数据校验列表');
insert into system_dict_type values(13, '发布状态', 'publish_status', '0', '小桐', sysdate(), '', NULL, '发布状态列表');
insert into system_dict_type values(14, '任务执行状态', 'ext_task_status', '0', '小桐', sysdate(), '', NULL, '任务执行状态列表');
insert into system_dict_type values(15, '数据源类型', 'datasource_type', '0', '小桐', sysdate(), '', NULL, '数据源类型列表');

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

insert into system_dict_data values(1,  1,  '男',       '0',       'sys_user_sex',        '',   '',        'Y', '0', '小桐', sysdate(), '', null, '性别男');
insert into system_dict_data values(2,  2,  '女',       '1',       'sys_user_sex',        '',   '',        'N', '0', '小桐', sysdate(), '', null, '性别女');
insert into system_dict_data values(3,  3,  '未知',     '2',       'sys_user_sex',        '',   '',        'N', '0', '小桐', sysdate(), '', null, '性别未知');
insert into system_dict_data values(4,  1,  '显示',     '0',       'sys_show_hide',       '',   'primary', 'Y', '0', '小桐', sysdate(), '', null, '显示菜单');
insert into system_dict_data values(5,  2,  '隐藏',     '1',       'sys_show_hide',       '',   'danger',  'N', '0', '小桐', sysdate(), '', null, '隐藏菜单');
insert into system_dict_data values(6,  1,  '正常',     '0',       'sys_normal_disable',  '',   'primary', 'Y', '0', '小桐', sysdate(), '', null, '正常状态');
insert into system_dict_data values(7,  2,  '停用',     '1',       'sys_normal_disable',  '',   'danger',  'N', '0', '小桐', sysdate(), '', null, '停用状态');
insert into system_dict_data values(8,  1,  '正常',     '0',       'sys_job_status',      '',   'primary', 'Y', '0', '小桐', sysdate(), '', null, '正常状态');
insert into system_dict_data values(9,  2,  '暂停',     '1',       'sys_job_status',      '',   'danger',  'N', '0', '小桐', sysdate(), '', null, '停用状态');
insert into system_dict_data values(10, 1,  '默认',     'DEFAULT', 'sys_job_group',       '',   '',        'Y', '0', '小桐', sysdate(), '', null, '默认分组');
insert into system_dict_data values(11, 2,  '系统',     'SYSTEM',  'sys_job_group',       '',   '',        'N', '0', '小桐', sysdate(), '', null, '系统分组');
insert into system_dict_data values(12, 1,  '是',       'Y',       'sys_yes_no',          '',   'primary', 'Y', '0', '小桐', sysdate(), '', null, '系统默认是');
insert into system_dict_data values(13, 2,  '否',       'N',       'sys_yes_no',          '',   'danger',  'N', '0', '小桐', sysdate(), '', null, '系统默认否');
insert into system_dict_data values(14, 1,  '通知',     '1',       'sys_notice_type',     '',   'warning', 'Y', '0', '小桐', sysdate(), '', null, '通知');
insert into system_dict_data values(15, 2,  '公告',     '2',       'sys_notice_type',     '',   'success', 'N', '0', '小桐', sysdate(), '', null, '公告');
insert into system_dict_data values(16, 1,  '正常',     '0',       'sys_notice_status',   '',   'primary', 'Y', '0', '小桐', sysdate(), '', null, '正常状态');
insert into system_dict_data values(17, 2,  '关闭',     '1',       'sys_notice_status',   '',   'danger',  'N', '0', '小桐', sysdate(), '', null, '关闭状态');
insert into system_dict_data values(18, 99, '其他',     '0',       'sys_oper_type',       '',   'info',    'N', '0', '小桐', sysdate(), '', null, '其他操作');
insert into system_dict_data values(19, 1,  '新增',     '1',       'sys_oper_type',       '',   'info',    'N', '0', '小桐', sysdate(), '', null, '新增操作');
insert into system_dict_data values(20, 2,  '修改',     '2',       'sys_oper_type',       '',   'info',    'N', '0', '小桐', sysdate(), '', null, '修改操作');
insert into system_dict_data values(21, 3,  '删除',     '3',       'sys_oper_type',       '',   'danger',  'N', '0', '小桐', sysdate(), '', null, '删除操作');
insert into system_dict_data values(22, 4,  '授权',     '4',       'sys_oper_type',       '',   'primary', 'N', '0', '小桐', sysdate(), '', null, '授权操作');
insert into system_dict_data values(23, 5,  '导出',     '5',       'sys_oper_type',       '',   'warning', 'N', '0', '小桐', sysdate(), '', null, '导出操作');
insert into system_dict_data values(24, 6,  '导入',     '6',       'sys_oper_type',       '',   'warning', 'N', '0', '小桐', sysdate(), '', null, '导入操作');
insert into system_dict_data values(25, 7,  '强退',     '7',       'sys_oper_type',       '',   'danger',  'N', '0', '小桐', sysdate(), '', null, '强退操作');
insert into system_dict_data values(26, 8,  '生成代码', '8',       'sys_oper_type',       '',   'warning', 'N', '0', '小桐', sysdate(), '', null, '生成操作');
insert into system_dict_data values(27, 9,  '清空数据', '9',       'sys_oper_type',       '',   'danger',  'N', '0', '小桐', sysdate(), '', null, '清空操作');
insert into system_dict_data values(28, 1,  '成功',     '0',       'sys_common_status',   '',   'primary', 'N', '0', '小桐', sysdate(), '', null, '正常状态');
insert into system_dict_data values(29, 2,  '失败',     '1',       'sys_common_status',   '',   'danger',  'N', '0', '小桐', sysdate(), '', null, '停用状态');

insert into system_dict_data values(30, 0, '文本', '0', 'ext_data_type', '', 'default', 'N', '0', '小桐', sysdate(), '', NULL, '文本类型');
insert into system_dict_data values(31, 1, '整数', '1', 'ext_data_type', '', 'default', 'N', '0', '小桐', sysdate(), '', NULL, '整数类型');
insert into system_dict_data values(32, 2, '小数', '2', 'ext_data_type', '', 'default', 'N', '0', '小桐', sysdate(), '', NULL, '小数类型');
insert into system_dict_data values(33, 3, '时间', '3', 'ext_data_type', '', 'default', 'N', '0', '小桐', sysdate(), '', NULL, '时间类型');
insert into system_dict_data values(34, 4, '字节类型', '4', 'ext_data_type', '', 'default', 'N', '0', '小桐', sysdate(), '', NULL, '字节类型');
insert into system_dict_data values(35, 5, '布尔值', '5', 'ext_data_type', '', 'default', 'N', '0', '小桐', sysdate(), '', NULL, '布尔值类型');
insert into system_dict_data values(36, 0, '唯一性校验', '0', 'ext_data_check', '', 'default', 'N', '0', '小桐', sysdate(), '', NULL, '唯一性校验');
insert into system_dict_data values(37, 1, '长度校验', '1', 'ext_data_check', '', 'default', 'N', '0', '小桐', sysdate(), '', NULL, '长度校验');
insert into system_dict_data values(38, 2, '区间校验', '2', 'ext_data_check', '', 'default', 'N', '0', '小桐', sysdate(), '', NULL, '区间校验');
insert into system_dict_data values(39, 0, '未发布', '0', 'publish_status', '', 'warning', 'N', '0', '小桐', sysdate(), '', NULL, '未发布状态');
insert into system_dict_data values(40, 1, '已发布', '1', 'publish_status', '', 'success', 'N', '0', '小桐', sysdate(), '', NULL, '已发布状态');
insert into system_dict_data values(41, 0, '未执行', '0', 'ext_task_status', '', 'primary', 'N', '0', '小桐', sysdate(), '', NULL, '未执行状态');
insert into system_dict_data values(42, 1, '进行中', '1', 'ext_task_status', '', 'warning', 'N', '0', '小桐', sysdate(), '', NULL, '进行中状态');
insert into system_dict_data values(43, 2, '已完成', '2', 'ext_task_status', '', 'success', 'N', '0', '小桐', sysdate(), '', NULL, '已完成状态');
insert into system_dict_data values(44, 3, '执行失败', '3', 'ext_task_status', '', 'danger', 'N', '0', '小桐', sysdate(), '', NULL, '执行失败状态');
insert into system_dict_data values (45, 1, 'MySql', 'MySql', 'datasource_type', '', 'primary', 'N', '0', '小桐', sysdate(), '', NULL, 'MySql数据库');
insert into system_dict_data values (46, 2, 'DM8', 'DM8', 'datasource_type', '', 'primary', 'N', '0', '小桐', sysdate(), '', NULL, '达梦8数据库');
insert into system_dict_data values (47, 3, 'Oracle', 'Oracle', 'datasource_type', '', 'primary', 'N', '0', '小桐', sysdate(), '', NULL, 'Oracle数据库');
insert into system_dict_data values (48, 4, 'Oracle11', 'Oracle11', 'datasource_type', '', 'primary', 'N', '0', '小桐', sysdate(), '', NULL, 'Oracle11数据库');
insert into system_dict_data values (49, 5, 'Kingbase8', 'Kingbase8', 'datasource_type', '', 'primary', 'N', '0', '小桐', sysdate(), '', NULL, '人大金仓8数据库');

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

insert into system_config values(1, '主框架页-默认皮肤样式名称',     'sys.index.skinName',            'skin-blue',     'Y', '小桐', sysdate(), '', null, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow' );
insert into system_config values(2, '用户管理-账号初始密码',         'sys.user.initPassword',         '123456',        'Y', '小桐', sysdate(), '', null, '初始化密码 123456' );
insert into system_config values(3, '主框架页-侧边栏主题',           'sys.index.sideTheme',           'theme-dark',    'Y', '小桐', sysdate(), '', null, '深色主题theme-dark，浅色主题theme-light' );
insert into system_config values(4, '账号自助-验证码开关',           'sys.account.captchaEnabled',    'true',          'Y', '小桐', sysdate(), '', null, '是否开启验证码功能（true开启，false关闭）');
insert into system_config values(5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser',      'false',         'Y', '小桐', sysdate(), '', null, '是否开启注册用户功能（true开启，false关闭）');
insert into system_config values(6, '用户登录-黑名单列表',           'sys.login.blackIPList',         '',              'Y', '小桐', sysdate(), '', null, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');


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

insert into system_job values(1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams',        '0/10 * * * * ?', '3', '1', '1', '小桐', sysdate(), '', null, '');
insert into system_job values(2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')',  '0/15 * * * * ?', '3', '1', '1', '小桐', sysdate(), '', null, '');
insert into system_job values(3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)',  '0/20 * * * * ?', '3', '1', '1', '小桐', sysdate(), '', null, '');
insert into system_job values (100, '非结构化任务抽取', 'DEFAULT', 'extUnstructTaskServiceImpl.consumeQueue()', '0 0/5 * * * ?', '1', '1', '1', '小桐', sysdate(), '', NULL, '');

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

insert into system_notice values('1', 'qKnow千知平台正式开源！', '2', '知识中心、知识抽取、知识图谱核心三大功能已发布', '0', '小桐', '2025-05-28 18:00:00', '', null, null);
insert into system_notice values('2', 'qKnow期待与您携手共建知识体系！', '1', '期待您的加入',   '0', '小桐', sysdate(), '2025-05-28 18:00:00', null, null);


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
                           create_time 	    datetime                                    comment '创建时间',
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
-- 20、系统内容配置
-- ----------------------------
drop table if exists system_content;
create table system_content (
                                  id int(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                  sys_name varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '系统名称',
                                  logo varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '系统logo',
                                  login_logo varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录页面logo',
                                  carousel_image varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '轮播图',
                                  contact_number varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
                                  email varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电子邮箱',
                                  copyright varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '版权方',
                                  record_number varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备案号',
                                  del_flag int(2) DEFAULT NULL COMMENT '删除标记',
                                  status int(2) DEFAULT NULL COMMENT '状态',
                                  create_by varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
                                  creator_id int(16) DEFAULT NULL COMMENT '创建人id',
                                  create_time datetime DEFAULT NULL COMMENT '创建时间',
                                  update_by varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
                                  updater_id int(16) DEFAULT NULL COMMENT '修改人id',
                                  update_time datetime DEFAULT NULL COMMENT '修改时间',
                                  remark varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

insert into system_content values (1, NULL, '', '', '', '400-660-8208', 'support@qiantong.tech', 'Copyright© 2025 江苏千桐科技有限公司 版权所有', '苏ICP备2022008519号-3', 0, NULL, NULL, NULL, NULL, '小桐', 1, '2025-01-13 13:18:06', NULL);


-- ----------------------------
-- 21、消息通知
-- ----------------------------
drop table if exists message;
create table message (
                           id int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                           sender_id bigint(20) DEFAULT NULL COMMENT '发送人',
                           receiver_id bigint(20) DEFAULT NULL COMMENT '接收人',
                           title varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息标题',
                           content varchar(3072) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息模板内容',
                           category tinyint(4) unsigned NOT NULL COMMENT '消息类别',
                           msg_level tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息等级',
                           module tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息模块',
                           entity_type tinyint(4) unsigned DEFAULT NULL COMMENT '实体类型',
                           entity_id bigint(20) DEFAULT NULL COMMENT '实体id',
                           entity_url varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息链接',
                           has_read tinyint(1) DEFAULT '0' COMMENT '是否已读',
                           has_retraction tinyint(1) DEFAULT '0' COMMENT '是否撤回',
                           valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                           del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                           create_by varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
                           creator_id bigint(20) DEFAULT NULL COMMENT '创建人id',
                           create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           update_by varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
                           update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                           updater_id bigint(20) DEFAULT NULL COMMENT '更新人id',
                           remark varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                           PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='消息';

-- ----------------------------
-- 22、数据源
-- ----------------------------
drop table if exists dm_datasource;
create table dm_datasource (
                                 id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                 datasource_name varchar(128) NOT NULL COMMENT '数据源名称',
                                 datasource_type varchar(32) NOT NULL DEFAULT '0' COMMENT '数据源类型',
                                 datasource_config varchar(1024) DEFAULT NULL COMMENT '数据源配置(json字符串)',
                                 ip varchar(32) NOT NULL COMMENT 'IP地址',
                                 port int NOT NULL COMMENT '端口号',
                                 list_count int DEFAULT NULL COMMENT '数据库表数（预留）',
                                 sync_count int DEFAULT NULL COMMENT '同步记录数（预留）',
                                 data_size int DEFAULT NULL COMMENT '同步数据量大小（预留）',
                                 description varchar(1024) DEFAULT NULL COMMENT '描述',
                                 valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                 del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                 create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                 creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                 create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                 updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                 update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                 remark varchar(512) DEFAULT NULL COMMENT '备注',
                                 PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='数据源';

insert into dm_datasource values (1, '本地数据库', 'MySql', '{\"username\":\"root\",\"password\":\"admin123\",\"dbname\":\"qknow_dev\"}', '127.0.0.1', 3306, NULL, NULL, NULL, NULL, 1, 0, '小桐', 1, sysdate(), '', NULL, sysdate(), NULL);


-- ----------------------------
-- 23、属性映射
-- ----------------------------
drop table if exists ext_attribute_mapping;
create table ext_attribute_mapping (
                                         id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                         workspace_id bigint NOT NULL COMMENT '工作区id',
                                         task_id bigint NOT NULL COMMENT '任务id',
                                         table_name varchar(128) NOT NULL COMMENT '表名',
                                         table_comment varchar(128) DEFAULT NULL COMMENT '表显示名称',
                                         field_name varchar(128) NOT NULL COMMENT '字段名',
                                         field_comment varchar(256) DEFAULT NULL COMMENT '字段显示名称',
                                         attribute_id bigint DEFAULT NULL COMMENT '属性id',
                                         attribute_name varchar(128) DEFAULT NULL COMMENT '属性名称',
                                         valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                         del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                         create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                         creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                         create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                         updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                         update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                         remark varchar(512) DEFAULT NULL COMMENT '备注',
                                         PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='属性映射';

insert into ext_attribute_mapping values (1, 1001, 1, 'system_role', '角色信息表', 'id', '角色ID', 4, '角色id', 0, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_attribute_mapping values (2, 1001, 1, 'system_role', '角色信息表', 'role_name', '角色名称', 5, '角色名称', 0, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_attribute_mapping values (3, 1001, 1, 'system_user', '用户信息表', 'id', '用户ID', 1, '用户id', 0, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_attribute_mapping values (4, 1001, 1, 'system_user', '用户信息表', 'user_name', '用户账号', 2, '用户名称', 0, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);


-- ----------------------------
-- 24、关系映射
-- ----------------------------
drop table if exists ext_relation_mapping;
create table ext_relation_mapping (
                                        id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        workspace_id bigint NOT NULL COMMENT '工作区id',
                                        task_id bigint NOT NULL COMMENT '任务id',
                                        table_name varchar(128) NOT NULL COMMENT '表名',
                                        table_comment varchar(128) DEFAULT NULL COMMENT '表显示名称',
                                        field_name varchar(128) NOT NULL COMMENT '字段名',
                                        field_comment varchar(256) DEFAULT NULL COMMENT '字段显示名称',
                                        relation varchar(128) NOT NULL COMMENT '关系',
                                        relation_table varchar(256) DEFAULT NULL COMMENT '关联表',
                                        relation_table_name varchar(128) DEFAULT NULL COMMENT '关联表名称',
                                        relation_field varchar(128) DEFAULT NULL COMMENT '关联表字段',
                                        relation_name_field varchar(128) DEFAULT NULL COMMENT '关联表实体字段',
                                        valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                        del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                        create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                        creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                        create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                        updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                        update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                        remark varchar(512) DEFAULT NULL COMMENT '备注',
                                        PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='关系映射';

insert into ext_relation_mapping values (1, 1001, 1, 'system_user', '用户信息表', 'role_id', '', '角色', 'system_role', '角色信息表', 'id', 'role_name', 0, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);


-- ----------------------------
-- 25、概念配置
-- ----------------------------
drop table if exists ext_schema;
create table ext_schema (
                              id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                              workspace_id bigint NOT NULL COMMENT '工作区id',
                              name varchar(128) NOT NULL COMMENT '概念名称',
                              description varchar(1024) DEFAULT NULL COMMENT '概念描述',
                              color varchar(32) DEFAULT NULL COMMENT '概念颜色',
                              valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                              del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                              create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                              creator_id bigint DEFAULT NULL COMMENT '创建人id',
                              create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                              updater_id bigint DEFAULT NULL COMMENT '更新人id',
                              update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                              remark varchar(512) DEFAULT NULL COMMENT '备注',
                              PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='概念配置';

insert into ext_schema values (1, 1001, '人物', NULL, '#006EFE', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema values (2, 1001, '歌曲', NULL, '#A109FF', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema values (3, 1001, '国家', NULL, '#FD0E02', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema values (4, 1001, '城市', NULL, '#FDB202', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema values (5, 1001, '企业', NULL, '#FF0FDF', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema values (7, 1001, '用户', NULL, '#0026FF', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema values (8, 1001, '角色', NULL, '#33FF00', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);


-- ----------------------------
-- 26、概念属性
-- ----------------------------
drop table if exists ext_schema_attribute;
create table ext_schema_attribute (
                                        id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        workspace_id bigint NOT NULL COMMENT '工作区id',
                                        schema_id bigint NOT NULL COMMENT '概念id',
                                        schema_name varchar(128) NOT NULL COMMENT '概念名称',
                                        name varchar(128) NOT NULL COMMENT '属性名称',
                                        name_code varchar(128) NOT NULL COMMENT '属性名称代码',
                                        require_flag tinyint(1) NOT NULL COMMENT '是否必填',
                                        data_type tinyint unsigned NOT NULL DEFAULT '0' COMMENT '数据类型;0：文本，1：整数，2：小数，3：时间，4：字节类型，5：布尔值',
                                        multiple_flag tinyint unsigned NOT NULL DEFAULT '0' COMMENT '单/多值;0：单值，1：多值',
                                        validate_type tinyint unsigned DEFAULT NULL COMMENT '校验方式;0：唯一性校验，1：长度校验，2：区间校验',
                                        min_value decimal(10,0) DEFAULT NULL COMMENT '最小值（用于区间校验）',
                                        max_value decimal(10,0) DEFAULT NULL COMMENT '最大值（用于区间校验）',
                                        valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                        del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                        create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                        creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                        create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                        updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                        update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                        remark varchar(512) DEFAULT NULL COMMENT '备注',
                                        PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='概念属性';

insert into ext_schema_attribute values (1, 1001, 7, '用户', '用户id', 'id', 1, 1, 0, 0, NULL, NULL, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema_attribute values (2, 1001, 7, '用户', '用户名称', 'user_name', 1, 0, 0, 1, NULL, 256, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema_attribute values (3, 1001, 7, '用户', '手机号码', 'phonenumber', 0, 0, 0, 1, NULL, 11, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema_attribute values (4, 1001, 8, '角色', '角色id', 'id', 1, 1, 0, NULL, NULL, NULL, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema_attribute values (5, 1001, 8, '角色', '角色名称', 'role_name', 0, 0, 0, NULL, NULL, NULL, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);


-- ----------------------------
-- 27、概念映射
-- ----------------------------
drop table if exists ext_schema_mapping;
create table ext_schema_mapping (
                                      id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                      workspace_id bigint NOT NULL COMMENT '工作区id',
                                      task_id bigint NOT NULL COMMENT '任务id',
                                      table_name varchar(128) NOT NULL COMMENT '表名',
                                      table_comment varchar(128) DEFAULT NULL COMMENT '表显示名称',
                                      entity_name_field varchar(32) DEFAULT NULL COMMENT '实体名称列',
                                      schema_id bigint DEFAULT NULL COMMENT '概念id',
                                      schema_name varchar(128) DEFAULT NULL COMMENT '概念名称',
                                      valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                      del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                      create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                      creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                      create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                      updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                      update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                      remark varchar(512) DEFAULT NULL COMMENT '备注',
                                      PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='概念映射';

insert into ext_schema_mapping values (1, 1001, 1, 'system_role', '角色信息表', 'role_name', 8, '角色', 0, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema_mapping values (2, 1001, 1, 'system_user', '用户信息表', 'user_name', 7, '用户', 0, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);


-- ----------------------------
-- 28、关系配置
-- ----------------------------
drop table if exists ext_schema_relation;
create table ext_schema_relation (
                                       id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                       workspace_id bigint NOT NULL COMMENT '工作区id',
                                       start_schema_id bigint NOT NULL COMMENT '起点概念id',
                                       relation varchar(128) NOT NULL COMMENT '关系',
                                       end_schema_id bigint NOT NULL COMMENT '终点概念id',
                                       inverse_flag tinyint(1) NOT NULL COMMENT '是否可逆',
                                       valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                       del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                       create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                       creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                       create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                       updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                       update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                       remark varchar(512) DEFAULT NULL COMMENT '备注',
                                       PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='关系配置';

insert into ext_schema_relation values (1, 1001, 1, '创作', 2, 0, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema_relation values (2, 1001, 1, '所属', 3, 0, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema_relation values (3, 1001, 4, '所属', 3, 0, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_schema_relation values (4, 1001, 5, '所属', 4, 0, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);


-- ----------------------------
-- 29、结构化抽取任务
-- ----------------------------
drop table if exists ext_struct_task;
create table ext_struct_task (
                                   id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                   workspace_id bigint NOT NULL COMMENT '工作区id',
                                   name varchar(128) NOT NULL COMMENT '任务名称',
                                   status tinyint unsigned NOT NULL DEFAULT '0' COMMENT '任务状态;0：未执行，1：进行中：2：已完成',
                                   publish_status tinyint unsigned NOT NULL DEFAULT '0' COMMENT '发布状态;0：未发布，1：已发布',
                                   publish_time datetime NOT NULL COMMENT '发布时间',
                                   publisher_id bigint NOT NULL COMMENT '发布人id',
                                   publish_by varchar(128) DEFAULT NULL COMMENT '发布人',
                                   datasource_id bigint NOT NULL COMMENT '数据源id',
                                   datasource_name varchar(128) NOT NULL COMMENT '数据源名称',
                                   valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                   del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                   create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                   creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                   create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                   updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                   update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                   remark varchar(512) DEFAULT NULL COMMENT '备注',
                                   PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='结构化抽取任务';

insert into ext_struct_task values (1, 1001, '用户信息抽取', 2, 1, sysdate(), 1, '小桐', 1, '本地数据库', 0, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);


-- ----------------------------
-- 30、非结构化抽取任务
-- ----------------------------
drop table if exists ext_unstruct_task;
create table ext_unstruct_task (
                                     id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                     workspace_id bigint NOT NULL COMMENT '工作区id',
                                     name varchar(128) NOT NULL COMMENT '任务名称',
                                     status tinyint unsigned NOT NULL DEFAULT '0' COMMENT '任务状态;0：未执行，1：进行中：2：已完成',
                                     publish_status tinyint unsigned NOT NULL DEFAULT '0' COMMENT '发布状态;0：未发布，1：已发布',
                                     publish_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                                     publisher_id bigint DEFAULT NULL COMMENT '发布人id',
                                     publish_by varchar(128) DEFAULT NULL COMMENT '发布人',
                                     valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                     del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                     create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                     creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                     create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                     updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                     update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                     remark varchar(512) DEFAULT NULL COMMENT '备注',
                                     PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='非结构化抽取任务';

insert into ext_unstruct_task values (1, 1001, '流行文化抽取', 2, 0, sysdate(), NULL, NULL, 1, 0, '小桐', 1, sysdate(), '小桐', NULL, sysdate(), NULL);
insert into ext_unstruct_task values (2, 1001, '科技创新抽取', 2, 1, sysdate(), 1, '小桐', 1, 0, '小桐', 1, sysdate(), '小桐', NULL, sysdate(), NULL);


-- ----------------------------
-- 31、任务文件关联表
-- ----------------------------
drop table if exists ext_unstruct_task_doc_rel;
create table ext_unstruct_task_doc_rel (
                                             id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                             workspace_id bigint NOT NULL COMMENT '工作区id',
                                             task_id bigint NOT NULL COMMENT '任务id',
                                             doc_id bigint NOT NULL COMMENT '文件id',
                                             doc_name varchar(128) NOT NULL COMMENT '文件名',
                                             valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                             del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                             create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                             creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                             create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                             update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                             updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                             update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                             remark varchar(512) DEFAULT NULL COMMENT '备注',
                                             PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='任务文件关联表';

insert into ext_unstruct_task_doc_rel values (1, 1001, 1, 1, '全球流行文化的交汇点.docx', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_doc_rel values (2, 1001, 2, 2, '硅谷的创新者与全球技术变革.docx', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);


-- ----------------------------
-- 32、任务关系关联表
-- ----------------------------
drop table if exists ext_unstruct_task_relation;
create table ext_unstruct_task_relation (
                                              id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                              workspace_id bigint NOT NULL COMMENT '工作区id',
                                              task_id bigint NOT NULL COMMENT '任务id',
                                              relation_id bigint NOT NULL COMMENT '关系id',
                                              valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                              del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                              create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                              creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                              create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                              update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                              updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                              update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                              remark varchar(512) DEFAULT NULL COMMENT '备注',
                                              PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='任务关系关联表';

insert into ext_unstruct_task_relation values (1, 1001, 1, 1, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_relation values (2, 1001, 1, 2, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_relation values (3, 1001, 1, 3, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_relation values (4, 1001, 1, 4, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_relation values (5, 1001, 2, 1, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_relation values (6, 1001, 2, 2, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_relation values (7, 1001, 2, 3, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_relation values (8, 1001, 2, 4, 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);


-- ----------------------------
-- 33、任务文件段落关联表
-- ----------------------------
drop table if exists ext_unstruct_task_text;
create table ext_unstruct_task_text (
                                          id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                          workspace_id bigint NOT NULL COMMENT '工作区id',
                                          task_id bigint NOT NULL COMMENT '任务id',
                                          doc_id bigint NOT NULL COMMENT '文件id',
                                          paragraph_index bigint NOT NULL COMMENT '段落标识',
                                          text text NOT NULL COMMENT '文字内容',
                                          valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                          del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                          create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                          creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                          create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                          updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                          update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                          remark varchar(512) DEFAULT NULL COMMENT '备注',
                                          PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='任务文件段落关联表';

insert into ext_unstruct_task_text values (1, 1001, 1, 1, 1, '歌手周杰伦创作了多首经典歌曲，如《稻香》，这首歌深受中国各地听众的喜爱。', 0, 0, '小桐', NULL, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_text values (2, 1001, 1, 1, 2, '在北京举办的华语乐坛盛事——第30届金曲奖颁奖典礼上，歌手邓紫棋凭借其歌曲《句号》获得了最佳女歌手奖。', 0, 0, '小桐', NULL, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_text values (3, 1001, 1, 1, 3, '上海是中国电影产业的重要基地之一，这里诞生了许多优秀的作品，比如由导演徐峥执导的电影《我不是药神》。', 0, 0, '小桐', NULL, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_text values (4, 1001, 2, 2, 1, '斯坦福大学位于美国加利福尼亚州硅谷，是一所世界领先的学府，培养了包括埃隆·马斯克在内的多位科技界领军人物。', 0, 0, '小桐', NULL, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_text values (5, 1001, 2, 2, 2, '谷歌DeepMind总部设在旧金山湾区，该公司由英国伦敦的研究团队创建，并因开发出击败围棋冠军的AI程序《AlphaGo》而闻名。', 0, 0, '小桐', NULL, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_text values (6, 1001, 2, 2, 3, '演员兼程序员梅丽莎·劳奇毕业于加州大学洛杉矶分校（UCLA），她曾在电视剧《生活大爆炸》中饰演科学家角色。', 0, 0, '小桐', NULL, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_text values (7, 1001, 2, 2, 4, '东京属于温带季风气候，同时也孕育了许多面向全球市场的人工智能初创公司，如总部位于该市的Preferred Networks。', 0, 0, '小桐', NULL, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_text values (8, 1001, 2, 2, 5, '日本歌手椎名林檎创作的歌曲《歌舞伎町女王》流行于东京都，同时她也为一部讲述人工智能伦理问题的日本电视综艺《未来人类》演唱过主题曲。', 0, 0, '小桐', NULL, sysdate(), '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_text values (9, 1001, 2, 2, 6, '中国杭州是互联网企业阿里巴巴集团的所在地，这座城市还以全年温和湿润的亚热带季风气候著称。', 0, 0, '小桐', NULL, '2025-05-27 13:35:02', '小桐', 1, sysdate(), NULL);
insert into ext_unstruct_task_text values (10, 1001, 2, 2, 10, '加拿大歌手格莱姆斯曾发布歌曲《We Appreciate Power》，探讨人工智能对社会的影响，并与埃隆·马斯克共同关注脑机接口技术的发展。', 0, 0, '小桐', NULL, sysdate(), '小桐', 1, sysdate(), NULL);


-- ----------------------------
-- 34、知识分类
-- ----------------------------
drop table if exists kmc_category;
create table kmc_category (
                                id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                workspace_id bigint NOT NULL COMMENT '工作区id',
                                parent_id bigint NOT NULL COMMENT '父级id',
                                name varchar(128) NOT NULL COMMENT '分类名称',
                                order_num int DEFAULT NULL COMMENT '显示顺序',
                                ancestors varchar(128) DEFAULT NULL COMMENT '祖级列表',
                                valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                remark varchar(512) DEFAULT NULL COMMENT '备注',
                                PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='知识分类';

insert into kmc_category values (1, 1001, 0, '文化与艺术', 1, '0', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (2, 1001, 0, '历史事件与人物', 2, '0', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (3, 1001, 0, '体育与娱乐', 3, '0', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (4, 1001, 0, '自然环境与生态保护', 4, '0', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (5, 1001, 0, '经济与发展', 5, '0', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (6, 1001, 0, '健康与医疗', 6, '0', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (7, 1001, 0, '科技创新与应用', 7, '0', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (8, 1001, 1, '文学', 1, '0,1', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (9, 1001, 1, '音乐与影视作品', 2, '0,1', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (10, 1001, 1, '视觉艺术', 3, '0,1', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (11, 1001, 1, '表演艺术', 4, '0,1', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (12, 1001, 2, '古代文明', 1, '0,2', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (13, 1001, 2, '近代历史', 2, '0,2', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (14, 1001, 2, '现代史', 3, '0,2', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (15, 1001, 3, '体育赛事', 1, '0,3', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (16, 1001, 3, '影视娱乐', 2, '0,3', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_category values (17, 1001, 3, '游戏产业', 3, '0,3', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);


-- ----------------------------
-- 35、知识文件表
-- ----------------------------
drop table if exists kmc_document;
create table kmc_document (
                                id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                workspace_id bigint NOT NULL COMMENT '工作区id',
                                category_id bigint NOT NULL COMMENT '知识分类id',
                                category_name varchar(128) DEFAULT NULL COMMENT '知识分类名称',
                                name varchar(256) NOT NULL COMMENT '文件名称',
                                path varchar(1024) NOT NULL COMMENT '文件路径',
                                description varchar(1024) DEFAULT NULL COMMENT '文件描述',
                                valid_flag tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                del_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                create_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                creator_id bigint DEFAULT NULL COMMENT '创建人id',
                                create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                update_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                updater_id bigint DEFAULT NULL COMMENT '更新人id',
                                update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                remark varchar(512) DEFAULT NULL COMMENT '备注',
                                PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='知识文件表';

insert into kmc_document values (1, 1001, 9, '音乐与影视作品', '全球流行文化的交汇点.docx', '/2025/05/27/683517a88fceebcb4928de44.docx', '用于测试非结构化抽取文件', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);
insert into kmc_document values (2, 1001, 7, '科技创新与应用', '硅谷的创新者与全球技术变革.docx', '/2025/05/27/683540a58fce4f307dbf6f0a.docx', '用于测试非结构化抽取文件', 1, 0, '小桐', 1, sysdate(), '小桐', 1, sysdate(), NULL);

