insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('admin','管理员',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('jybsgqd','就医北上广渠道',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('jybsgsc','就医北上广市场',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('jybsgpt','就医北上广平台',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('ys','医生',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('hs','护士',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('ghy','挂号员',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('sfy','收费员',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('fyy','发药员',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('cw','财务',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('qtry','其他人员',0,1);


delete from t_p_s_role_vs_permission where rid = (select id from t_p_s_role where rolecode = 'admin');

insert into t_p_s_role_vs_permission(pid,rid)
select id,(select id from t_p_s_role where rolecode = 'admin' )
 from t_p_s_permission t1 where pid in (select id from t_p_s_permission where pid is  null)

-- 创建就医北上广渠道角色
insert into t_p_s_role_vs_permission(pid,rid)
select id,(select id from t_p_s_role where rolecode = 'jybsgqd' )
 from t_p_s_permission t1 where percode ='qd' OR percode ='jybsg'
-- 创建就医北上广市场角色
insert into t_p_s_role_vs_permission(pid,rid)
select id,(select id from t_p_s_role where rolecode = 'jybsgsc' )
 from t_p_s_permission t1 where percode ='sc' OR percode ='jybsg'

-- 创建就医北上广市场角色
insert into t_p_s_role_vs_permission(pid,rid)
select id,(select id from t_p_s_role where rolecode = 'jybsgpt' )
 from t_p_s_permission t1 where percode ='pt' OR percode ='jybsg'
-- 传角色id 可以初始化成机构管理员
insert into t_p_s_role_vs_permission(pid,rid)
        select id,#{rid}
        from t_p_s_permission t1 where pid in (select id from t_p_s_permission where pid is null) AND hideflag = 0