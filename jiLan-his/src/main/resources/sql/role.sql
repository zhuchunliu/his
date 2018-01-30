insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('admin','管理员',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('ys','医生',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('hs','护士',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('ghy','挂号员',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('sfy','收费员',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('fyy','发药员',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('cw','财务',0,1);
insert into t_p_s_role(rolecode,rolename,removed,isvalid) values('qtry','其他人员',0,1);

insert into t_p_s_role_vs_permission(pid,rid)
select id,(select id from t_p_s_role where rolecode = 'admin' )
 from t_p_s_permission t1 where pid in (select id from t_p_s_permission where pid is  null)
