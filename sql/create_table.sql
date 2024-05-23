# 数据库初始化

-- 创建库
create database if not exists ZOJ;

-- 切换库
use ZOJ;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';

-- 评论表
create table if not exists comments
(
    id         bigint auto_increment comment 'id' primary key,
    content    text                               null comment '内容',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    postId     bigint                             not null comment '帖子 id',
    questionId bigint                             not null comment '题目 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_postId (postId),
    index idx_userId (userId),
    index idx_questionId(questionId)
) comment '帖子' collate = utf8mb4_unicode_ci;
-- 评论表
create table if not exists comment
(
    id         bigint auto_increment comment 'id' primary key,
    content    text                               null comment '内容',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户id',
    userName   varchar(256)                           null comment '用户昵称',
    foreignId bigint                             not null comment '模块id',
    pid        bigint                             null     comment '父级评论id',
    target     varchar(256)                       null comment '回复对象',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_postId (foreignId),
    index idx_userId (userId),
    index idx_questionId(pid)
) comment '评论' collate = utf8mb4_unicode_ci;
-- 题目表
create table if not exists question
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '题目标题',
    content     text                               null comment '题目内容',
    tags        varchar(1024)                      null comment '题目标签列表（json 数组）',
    answer      text                               null comment '题目答案',
    submitNum   int      default 0                 null comment '题目提交数',
    acceptNum   int      default 0                 null comment '题目通过数',
    judgeCase   text                               null comment '判题用例（json 数组）',
    judgeConfig text                               null comment '判题配置（json 对象）',
    thumbNum    int      default 0                 not null comment '点赞数',
    favourNum   int      default 0                 not null comment '收藏数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 题单表
create table if not exists question_list
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '题单标题',
    content     text                               null comment '题单内容',
    tags        varchar(1024)                      null comment '题单标签列表（json 数组）',
    questionCase   text                            null comment '题目用例（json 数组）',
    thumbNum    int      default 0                 not null comment '点赞数',
    favourNum   int      default 0                 not null comment '收藏数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    activeTime  datetime default CURRENT_TIMESTAMP not null comment '持续时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '题单' collate = utf8mb4_unicode_ci;
# judgeCase 判题用例（json 数组）
# 每一个元素是：一个输入用例对应一个输出用例
# [
#   {
#     "input": "1 2",
#     "output": "3 4"
#   },
#   {
#     "input": "1 3",
#     "output": "2 4"
#   }
# ]
# judgeConfig 判题配置（json 对象）：
# 时间限制 timeLimit
# 内存限制 memoryLimit
# 存 json 的好处：便于扩展，只需要改变对象内部的字段，而不用修改数据库表（可能会影响数据库）
# {
#   "timeLimit": 1000,
#   "memoryLimit": 1000,
#   "stackLimit": 1000
# }
-- 题目提交表
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)                       not null comment '编程语言',
    code       text                               not null comment '用户代码',
    judgeInfo  text                               null comment '判题信息（json 对象）',
    status     int      default 0                 not null comment '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目提交';
-- 题目信息表
create table if not exists judgeinfo
(
    id         bigint auto_increment comment 'id' primary key,
    message    text                      not null comment '程序执行信息',
    memory      int                               not null comment '消耗内存（KB）',
    times       int                               null comment '消耗时间（ms）',
    status     int      default 0                 not null comment '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
    ) comment '题目信息';
-- 题目题解表
create table if not exists questionanswer
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)                       not null comment '编程语言',
    message    text                       not null comment '程序执行信息',
    code       text                               not null comment '用户代码',
    memory      int                               not null comment '消耗内存（KB）',
    times       int                               not null comment '消耗时间（ms）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目的题解';
-- 用户排行表
create table if not exists user_rank
(
    id           bigint auto_increment comment 'id' primary key,
    userId       bigint                             not null comment '用户 id',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    acNum        int                                    not null comment '过题总数',
    submitNum    int                                    not null comment '提交题目总数',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '用户排行';
-- 题目大用例表
create table if not exists questionJudgeCase
(
    id          bigint auto_increment comment 'id' primary key,
    judgeCase1   text                               null comment '判题用例（json 数组）',
    judgeCase2   text                               null comment '判题用例（json 数组）',
    judgeConfig text                               null comment '判题配置（json 对象）',
    userId      bigint                             not null comment '用户 id',
    questionId  bigint                             not null comment '题目 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目大用例' collate = utf8mb4_unicode_ci;
# 判题信息（判题过程中得到的一些信息，比如程序的失败原因、程序执行消耗的时间、空间）：
# judgeInfo（json 对象）
# {
#   "message": "程序执行信息",
#   "time": 1000, // 单位为 ms
#   "memory": 1000, // 单位为 kb
# }
# 判题信息枚举值：
# Accepted 成功
# Wrong Answer 答案错误
# Compile Error 编译错误
# Memory Limit Exceeded 内存溢出
# Time Limit Exceeded 超时
# Presentation Error 展示错误
# Output Limit Exceeded 输出溢出
# Waiting 等待中
# Dangerous Operation 危险操作
# Runtime Error 运行错误（用户程序的问题）
# System Error 系统错误（做系统人的问题）
# 小知识 - 数据库索引
# 什么情况下适合加索引？如何选择给哪个字段加索引？
# 答：首先从业务出发，无论是单个索引、还是联合索引，都要从你实际的查询语句、字段枚举值的区分度、字段的类型考虑（where 条件指定的字段）
#
# 比如：where userId = 1 and questionId = 2
# 可以选择根据 userId 和 questionId 分别建立索引（需要分别根据这两个字段单独查询）；也可以选择给这两个字段建立联合索引（所查询的字段是绑定在一起的）。
# 原则上：能不用索引就不用索引；能用单个索引就别用联合 / 多个索引；不要给没区分度的字段加索引（比如性别，就男 / 女）。因为索引也是要占用空间的。