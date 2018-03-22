-- 一级菜单
insert into t_p_s_permission(percode,pername,sn) values('kbjz','看病就诊',1) ;
insert into t_p_s_permission(percode,pername,sn) values('sffy','收费发药',2) ;
insert into t_p_s_permission(percode,pername,sn) values('ypjxc','药品进销存',3) ;
insert into t_p_s_permission(percode,pername,sn) values('sjtj','数据统计',4) ;
insert into t_p_s_permission(percode,pername,sn) values('xtsz','系统设置',5) ;


-- 二级菜单
insert into t_p_s_permission(percode,pername,sn,pid) select 'mzlb','门诊列表',1, id from t_p_s_permission where percode = 'kbjz' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'yspb','医生排班',2, id from t_p_s_permission where percode = 'kbjz' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'ghlb','挂号列表',3, id from t_p_s_permission where percode = 'kbjz' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'hzk','患者库',4,	id from t_p_s_permission where percode = 'kbjz' ;

insert into t_p_s_permission(percode,pername,sn,pid) select 'sffy','收费发药',1, id from t_p_s_permission where percode = 'sffy' and pid is NULL ;

insert into t_p_s_permission(percode,pername,sn,pid) select 'ypxxwh','药品信息维护',1, id from t_p_s_permission where percode = 'ypjxc' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'gyswh','供应商维护',2, id from t_p_s_permission where percode = 'ypjxc' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'cgrk','采购入库',3, id from t_p_s_permission where percode = 'ypjxc' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'rksh','入库审核',4, id from t_p_s_permission where percode = 'ypjxc' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'kcpd','库存盘点',5, id from t_p_s_permission where percode = 'ypjxc' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'pdsh','盘点审核',6, id from t_p_s_permission where percode = 'ypjxc' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'kccx','库存查询',7, id from t_p_s_permission where percode = 'ypjxc' ;

insert into t_p_s_permission(percode,pername,sn,pid) select 'zssztj','诊所收支统计',1, id from t_p_s_permission where percode = 'sjtj' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'ypxstj','药品销售统计',2, id from t_p_s_permission where percode = 'sjtj' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'jcxmtj','检查项目统计',3, id from t_p_s_permission where percode = 'sjtj' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'nybbtj','年月报表统计',4, id from t_p_s_permission where percode = 'sjtj' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'gzltj','工作量统计',5, id from t_p_s_permission where percode = 'sjtj' ;

insert into t_p_s_permission(percode,pername,sn,pid) select 'ysgl','医生管理',1, id from t_p_s_permission where percode = 'xtsz' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'ksgl','科室管理',2, id from t_p_s_permission where percode = 'xtsz' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'mbwh','模板维护',3, id from t_p_s_permission where percode = 'xtsz' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'fygl','费用管理',4, id from t_p_s_permission where percode = 'xtsz' ;
insert into t_p_s_permission(percode,pername,sn,pid,hideflag) select 'zdbwh','字典表维护',5, id,1 from t_p_s_permission where percode = 'xtsz' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'zsxx','诊所信息',6, id from t_p_s_permission where percode = 'xtsz' ;
insert into t_p_s_permission(percode,pername,sn,pid) select 'qxgl','权限管理',7, id from t_p_s_permission where percode = 'xtsz' ;
insert into t_p_s_permission(percode,pername,sn,pid,hideflag) select 'scsgl','生产商管理',8, id,1 from t_p_s_permission where percode = 'xtsz' ;
insert into t_p_s_permission(percode,pername,sn,pid,hideflag) select 'gysgl','供应商管理',9, id,1 from t_p_s_permission where percode = 'xtsz' ;
insert into t_p_s_permission(percode,pername,sn,pid,hideflag) select 'jggl','机构管理',10, id,1 from t_p_s_permission where percode = 'xtsz' ;
insert into t_p_s_permission(percode,pername,sn,pid,hideflag) select 'ypzdwh','药品字典维护',11, id,1 from t_p_s_permission where percode = 'xtsz' ;



-- 门诊列表
insert into t_p_s_permission(pername,url,pid) select '添加/编辑处方','/pre',id from t_p_s_permission where percode = 'mzlb';
insert into t_p_s_permission(pername,url,pid) select '处方列表','/pre/list',id from t_p_s_permission where percode = 'mzlb';
insert into t_p_s_permission(pername,url,pid) select '门诊列表页面','/apply/tiaojianchaxun',id from t_p_s_permission where percode = 'mzlb';
insert into t_p_s_permission(pername,url,pid) select '医生帮忙挂号','/apply/addByDoctor ',id from t_p_s_permission where percode = 'mzlb';
insert into t_p_s_permission(pername,url,pid) select '挂号单详情','/apply/id',id from t_p_s_permission where percode = 'mzlb';
insert into t_p_s_permission(pername,url,pid) select '查询某机构的初诊数和就诊数','/apply/chuZhenAndFuZhenTongJi',id from t_p_s_permission where percode = 'mzlb';
insert into t_p_s_permission(pername,url,pid) select '现金收挂号费','/apply/cashcollection',id from t_p_s_permission where percode = 'mzlb';
insert into t_p_s_permission(pername,url,pid) select '现金退号','/apply/cashrefund',id from t_p_s_permission where percode = 'mzlb';


-- 医生排班
insert into t_p_s_permission(pername,url,pid) select '设置排班信息','/schedule/save',id from t_p_s_permission where percode = 'yspb';
insert into t_p_s_permission(pername,url,pid) select '获取排班列表','/schedule/list',id from t_p_s_permission where percode = 'yspb';
insert into t_p_s_permission(pername,url,pid) select '获取上周排班信息','/schedule/previous',id from t_p_s_permission where percode = 'yspb';


-- 挂号列表
insert into t_p_s_permission(pername,url,pid) select '医生查看患者库中某位患者的挂号列表','/apply/patientItemId',id from t_p_s_permission where percode = 'ghlb';

-- 患者库
insert into t_p_s_permission(pername,url,pid) select '查看患者库','/patientItem/getPatientPoolByPage',id from t_p_s_permission where percode = 'hzk';
insert into t_p_s_permission(pername,url,pid) select '添加黑名单','/patientItem/addPatientBlacklist',id from t_p_s_permission where percode = 'hzk';
insert into t_p_s_permission(pername,url,pid) select '移除黑名单','/patientItem/removedPatientBlacklist',id from t_p_s_permission where percode = 'hzk';
insert into t_p_s_permission(pername,url,pid) select '编辑患者库信息','/patientItem/update',id from t_p_s_permission where percode = 'hzk';
insert into t_p_s_permission(pername,url,pid) select '患者详情','/patientItem/id',id from t_p_s_permission where percode = 'hzk';


-- 收费发药
insert into t_p_s_permission(pername,url,pid) select '收支概况','/dispens/fee/survey',id from t_p_s_permission where percode = 'sffy' and pid is NOT NULL;
insert into t_p_s_permission(pername,url,pid) select '收费发药列表','/dispens/list',id from t_p_s_permission where percode = 'sffy' and pid is NOT NULL;
insert into t_p_s_permission(pername,url,pid) select '付费费用列表','/dispens/pay/fee',id from t_p_s_permission where percode = 'sffy' and pid is NOT NULL;
insert into t_p_s_permission(pername,url,pid) select '付费','/dispens/pay',id from t_p_s_permission where percode = 'sffy' and pid is NOT NULL;
insert into t_p_s_permission(pername,url,pid) select '确认发药','/dispens',id from t_p_s_permission where percode = 'sffy' and pid is NOT NULL;
insert into t_p_s_permission(pername,url,pid) select '退款','/dispens/refund',id from t_p_s_permission where percode = 'sffy' and pid is NOT NULL;
insert into t_p_s_permission(pername,url,pid) select '退费列表','/dispens/refund/list',id from t_p_s_permission where percode = 'sffy' and pid is NOT NULL;
insert into t_p_s_permission(pername,url,pid) select '发药','/dispens/medicine',id from t_p_s_permission where percode = 'sffy' and pid is NOT NULL;
insert into t_p_s_permission(pername,url,pid) select '发药列表','/dispens/medicine/list',id from t_p_s_permission where percode = 'sffy' and pid is NOT NULL;



-- 药品信息维护
insert into t_p_s_permission(pername,url,pid) select '删除药品','/drug/del',id from t_p_s_permission where percode = 'ypxxwh';
insert into t_p_s_permission(pername,url,pid) select '批量添加药品信息','/drug/batch',id from t_p_s_permission where percode = 'ypxxwh';
insert into t_p_s_permission(pername,url,pid) select '更新药品信息','/drug/update',id from t_p_s_permission where percode = 'ypxxwh';
insert into t_p_s_permission(pername,url,pid) select '调价/调整库存','/drug/stock/modify',id from t_p_s_permission where percode = 'ypxxwh';


-- 供应商维护 --
insert into t_p_s_permission(pername,url,pid) select '添加/编辑','/supply/save',id from t_p_s_permission where percode = 'gyswh';
insert into t_p_s_permission(pername,url,pid) select '详情','/supply/id',id from t_p_s_permission where percode = 'gyswh';


-- 采购入库
insert into t_p_s_permission(pername,url,pid) select '采购入库','/purchase/save',id from t_p_s_permission where percode = 'cgrk';

-- 入库审核
insert into t_p_s_permission(pername,url,pid) select '入库审核列表','/purchase/audit/list',id from t_p_s_permission where percode = 'rksh';
insert into t_p_s_permission(pername,url,pid) select '审核入库','/purchase/audit',id from t_p_s_permission where percode = 'rksh';
insert into t_p_s_permission(pername,url,pid) select '删除入库信息','/purchase/del',id from t_p_s_permission where percode = 'rksh';

-- 库存查询
insert into t_p_s_permission(pername,url,pid) select '库存查询','/drug/stock',id from t_p_s_permission where percode = 'kccx';
insert into t_p_s_permission(pername,url,pid) select '批次库存查询','/batch',id from t_p_s_permission where percode = 'kccx';



-- 诊所收支统计
insert into t_p_s_permission(pername,url,pid) select '收支明细','/waterDay/detailList',id from t_p_s_permission where percode = 'zssztj';


-- 药品销售统计
insert into t_p_s_permission(pername,url,pid) select '当天出入库实时统计','/report/drug/recent',id from t_p_s_permission where percode = 'ypxstj';
insert into t_p_s_permission(pername,url,pid) select '收支概况','/report/drug/survey',id from t_p_s_permission where percode = 'ypxstj';
insert into t_p_s_permission(pername,url,pid) select '药品出库统计','/report/sale/page',id from t_p_s_permission where percode = 'ypxstj';
insert into t_p_s_permission(pername,url,pid) select '药品出库统计','/report/sale/list',id from t_p_s_permission where percode = 'ypxstj';
insert into t_p_s_permission(pername,url,pid) select '药品出库明细','/report/sale/detail',id from t_p_s_permission where percode = 'ypxstj';
insert into t_p_s_permission(pername,url,pid) select '药品入库列表','/report/purchase/page',id from t_p_s_permission where percode = 'ypxstj';
insert into t_p_s_permission(pername,url,pid) select '药品入库列表','/report/purchase/list',id from t_p_s_permission where percode = 'ypxstj';
insert into t_p_s_permission(pername,url,pid) select '药品入库明细','/report/purchase/detail',id from t_p_s_permission where percode = 'ypxstj';


-- 检查项目统计
insert into t_p_s_permission(pername,url,pid) select '当天检查项实时统计','/report/inspect/recent',id from t_p_s_permission where percode = 'jcxmtj';
insert into t_p_s_permission(pername,url,pid) select '收支概况','/report/inspect/survey',id from t_p_s_permission where percode = 'jcxmtj';
insert into t_p_s_permission(pername,url,pid) select '检查统计','/report/inspect/list',id from t_p_s_permission where percode = 'jcxmtj';
insert into t_p_s_permission(pername,url,pid) select '检查统计','/report/inspect/detail',id from t_p_s_permission where percode = 'jcxmtj';

-- 年月报表统计
insert into t_p_s_permission(pername,url,pid) select '区间列表','/waterDay/getListBetweenTimes',id from t_p_s_permission where percode = 'nybbtj';
insert into t_p_s_permission(pername,url,pid) select '区间内数据综和','/waterDay/getListBetweenTimesSum',id from t_p_s_permission where percode = 'nybbtj';
insert into t_p_s_permission(pername,url,pid) select 'pc端诊所收支统计汇总','/waterDay/zhensuoShouzhiCount',id from t_p_s_permission where percode = 'nybbtj';
insert into t_p_s_permission(pername,url,pid) select '机构第一条报表数据','/waterDay/firstWaterDay',id from t_p_s_permission where percode = 'nybbtj';
insert into t_p_s_permission(pername,url,pid) select '获取昨日和昨日所在月收益','/waterDay/getWaterDayAndMonthCount',id from t_p_s_permission where percode = 'nybbtj';
insert into t_p_s_permission(pername,url,pid) select '年月报表','/waterDay/yearMonthBaobiao',id from t_p_s_permission where percode = 'nybbtj';


-- 工作量统计
insert into t_p_s_permission(pername,url,pid) select '工作量统计','/report/list',id from t_p_s_permission where percode = 'gzltj';
insert into t_p_s_permission(pername,url,pid) select '预约量医生排名列表','/report/doctorOrder',id from t_p_s_permission where percode = 'gzltj';
insert into t_p_s_permission(pername,url,pid) select '按天分组的预约统计信息列表','/report/yuyuelist',id from t_p_s_permission where percode = 'gzltj';
insert into t_p_s_permission(pername,url,pid) select '诊所预约统计','/report/orgCount',id from t_p_s_permission where percode = 'gzltj';




-- 医生管理
-- insert into t_p_s_permission(pername,url,pid) select '新增/编辑 用户信息','/doctor/save',id from t_p_s_permission where percode = 'ysgl';
insert into t_p_s_permission(pername,url,pid) select '获取用户列表','/doctor/list',id from t_p_s_permission where percode = 'ysgl';
insert into t_p_s_permission(pername,url,pid) select '删除用户信息','/doctor/del',id from t_p_s_permission where percode = 'ysgl';
insert into t_p_s_permission(pername,url,pid) select '禁用、启用 用户','/doctor/switch',id from t_p_s_permission where percode = 'ysgl';


-- 科室管理
insert into t_p_s_permission(pername,url,pid) select '新增/编辑 科室信息','/dept/save',id from t_p_s_permission where percode = 'ksgl';
insert into t_p_s_permission(pername,url,pid) select '删除科室信息','/dept/del',id from t_p_s_permission where percode = 'ksgl';


-- 模板维护
insert into t_p_s_permission(pername,url,pid) select '新增/编辑 诊断模板','/tpl/diagnosis/save',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '删除诊断模板','/tpl/diagnosis/del',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '禁用/启用 诊断模板','/tpl/diagnosis/switch',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '新增/编辑 医嘱模板','/tpl/advice/save',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '删除医嘱模板','/tpl/advice/del',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '禁用/启用 医嘱模板','/tpl/advice/switch',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '新增/编辑 处方模板','/tpl/prescripTpl/save',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '禁用/启用 处方模板','/tpl/prescripTpl/switch',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '删除 处方模板','/tpl/prescripTpl/del',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '编辑 处方模板-药品','/tpl/prescripTpl/item/save',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '编辑 处方模板-检查','/tpl/prescripTpl/inspect/save',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '删除病例模板','/medicalRecordTpl/del',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '恢复病例模板','/medicalRecordTpl/recover',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '禁用病例模板','/medicalRecordTpl/forbidden',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '启用病例模板','/medicalRecordTpl/using',id from t_p_s_permission where percode = 'mbwh';
insert into t_p_s_permission(pername,url,pid) select '新增/编辑病例模板','/medicalRecordTpl/save',id from t_p_s_permission where percode = 'mbwh';


-- 费用管理
insert into t_p_s_permission(pername,url,pid) select '新增/编辑 收费项目信息','/fee/save',id from t_p_s_permission where percode = 'fygl';
insert into t_p_s_permission(pername,url,pid) select '删除收费项目信息','/fee/del',id from t_p_s_permission where percode = 'fygl';


-- 字典表维护
insert into t_p_s_permission(pername,url,pid) select '添加字典','/dicItem/add',id from t_p_s_permission where percode = 'zdbwh';
insert into t_p_s_permission(pername,url,pid) select '删除字典','/dicItem/removed',id from t_p_s_permission where percode = 'zdbwh';
insert into t_p_s_permission(pername,url,pid) select '编辑字典','/dicItem/save',id from t_p_s_permission where percode = 'zdbwh';

-- 诊所信息
insert into t_p_s_permission(pername,url,pid) select '新增/编辑 机构信息','/org/save',id from t_p_s_permission where percode = 'zsxx';

-- 生产商管理
insert into t_p_s_permission(pername,url,pid) select '添加/编辑 ','/manufacturer/save',id from t_p_s_permission where percode = 'scsgl';

-- 供应商管理
insert into t_p_s_permission(pername,url,pid) select '添加/编辑 ','/supply/save',id from t_p_s_permission where percode = 'gysgl';

-- 机构管理
insert into t_p_s_permission(pername,url,pid) select '添加/编辑 ','/org/save',id from t_p_s_permission where percode = 'jggl';
insert into t_p_s_permission(pername,url,pid) select '删除 ','/org/del',id from t_p_s_permission where percode = 'jggl';