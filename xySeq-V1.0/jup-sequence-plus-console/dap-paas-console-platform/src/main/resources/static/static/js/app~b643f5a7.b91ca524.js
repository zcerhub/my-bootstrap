(window["jsonpPaas4"]=window["jsonpPaas4"]||[]).push([["app~b643f5a7"],{"28df":function(t,e,a){},"2e3f":function(t,e,a){"use strict";a("28df")},3991:function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=t._self._c;return e("div",{staticClass:"g-page"},[e("div",{},[e("el-form",{ref:"formRef",staticClass:"demo-form-inline",attrs:{inline:!0,model:t.requestObject,rules:t.rules}},[e("el-form-item",{staticClass:"mr0",attrs:{label:"IP"}},[e("el-input",{attrs:{placeholder:"请输入IP"},model:{value:t.requestObject.ipAddress,callback:function(e){t.$set(t.requestObject,"ipAddress",e)},expression:"requestObject.ipAddress"}})],1),e("el-form-item",{staticClass:"mr0",attrs:{label:"环境",prop:"envId"}},[e("el-select",{attrs:{loading:t.loadings.env},on:{change:t.changeEnvId},model:{value:t.requestObject.envId,callback:function(e){t.$set(t.requestObject,"envId",e)},expression:"requestObject.envId"}},t._l(t.envOptions,(function(t){return e("el-option",{key:t.value,attrs:{value:t.value,label:t.label,filterable:""}})})),1)],1),e("el-form-item",[e("el-button",{attrs:{inline:"true",type:"primary"},on:{click:function(e){return t.clickSearchButton(!1)}}},[t._v("查询")]),e("el-button",{attrs:{inline:"true"},on:{click:function(e){return t.clickSearchButton(!0)}}},[t._v("重置")])],1)],1)],1),e("tableView",{attrs:{loading:t.tableConfig.loading,operateObj:t.tableConfig.operate,columns:t.tableConfig.columns,tableData:t.tableConfig.tableData,isPage:t.tableConfig.isPage,total:t.tableConfig.pagination.totalCount,rowKey:"id",currentPage:t.tableConfig.pagination.pageNo,pageSize:t.tableConfig.pagination.pageSize},on:{"update:currentPage":function(e){return t.$set(t.tableConfig.pagination,"pageNo",e)},"update:current-page":function(e){return t.$set(t.tableConfig.pagination,"pageNo",e)},"update:pageSize":function(e){return t.$set(t.tableConfig.pagination,"pageSize",e)},"update:page-size":function(e){return t.$set(t.tableConfig.pagination,"pageSize",e)},changePage:t.changePage,operate:t.getIndex}}),t.visible?e("AccountAdd",{attrs:{rowData:t.rowData,refresh:t.getList},model:{value:t.visible,callback:function(e){t.visible=e},expression:"visible"}}):t._e()],1)},i=[],o=a("5530"),l=(a("7db0"),a("d3b7"),a("c13c")),c=a("c43b"),s=a("7b40"),r=a("ff51"),u=a("ec41"),g=a("ed08"),p={name:"manage-account",components:{AccountAdd:l["default"]},mixins:[],props:{},data:function(){return{loadings:{env:!1},requestObject:{ipAddress:"",envId:"",envCode:""},rules:{envId:[{required:!0,trigger:"change",message:"请选择环境"}]},envOptions:[],visible:!1,tableConfig:{loading:!1,pagination:{pageNo:1,pageSize:10,totalCount:0},tableData:[],columns:[{prop:"ipAddress",label:"IP"},{prop:"appCode",label:"应用名称"},{prop:"envCode",label:" 环境名称"},{prop:"configName",label:"配置名称"},{prop:"updateDate",label:"修改时间",format:function(t){return Object(g["d"])(t)}}],operate:{width:160,list:[{label:"配置追踪",id:"edit"}]}},rowData:null}},computed:{},watch:{},created:function(){this.getEnvOptions()},mounted:function(){},methods:{getEnvOptions:function(){var t=this;this.loadings.env=!0,Object(c["f"])().then((function(e){t.envOptions=Object(r["a"])(null===e||void 0===e?void 0:e.data)?e.data:[]})).catch((function(t){})).finally((function(){t.loadings.env=!1}))},changeEnvId:function(t){var e=this.envOptions.find((function(e){return e.value===t}));this.requestObject.envCode=e.label},getList:function(){var t=this,e=Object(o["a"])(Object(o["a"])({},this.tableConfig.pagination),{},{requestObject:this.requestObject});this.tableConfig.loading=!0,Object(s["a"])(e).then((function(e){var a=Object(u["a"])(e,"data.data");Object(r["a"])(a)&&(t.tableConfig.tableData=a),t.tableConfig.pagination.totalCount=Object(u["a"])(e,"data.totalCount")||0})).catch((function(t){})).finally((function(){t.tableConfig.loading=!1}))},getIndex:function(t){t.index;var e=t.row,a=t.operate;"delete"===a.id?this.$confirm("确定要删除【".concat(e.userName,"】吗?"),{type:"warning"}).then((function(){})).catch((function(t){})):"edit"===a.id&&(this.rowData=e,this.visible=!0)},clickSearchButton:function(t){t?(this.$refs.formRef.resetFields(),this.tableConfig.pagination.pageNo=1,this.tableConfig.tableData=[],this.tableConfig.pagination.totalCount=0):this.getList()},openAdd:function(){this.rowData=null,this.visible=!0},changePage:function(t,e){t&&(this.tableConfig.pagination.pageNo=t),e&&(this.tableConfig.pagination.pageNo=1,this.tableConfig.pagination.pageSize=e),this.getList()}}},d=p,f=a("2877"),b=Object(f["a"])(d,n,i,!1,null,"7786005c",null);e["default"]=b.exports},4145:function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=t._self._c;return e("el-dialog-limit",{attrs:{title:"查看实例数",visible:t.visible,size:"medium","append-to-body":!0,"close-on-click-modal":!1},on:{close:t.clickCancel}},[e("tableView",{attrs:{loading:t.tableConfig.loading,columns:t.tableConfig.columns,tableData:t.tableConfig.tableData,isPage:t.tableConfig.isPage,total:t.tableConfig.pagination.totalCount,rowKey:"id",currentPage:t.tableConfig.pagination.pageNo,pageSize:t.tableConfig.pagination.pageSize},on:{"update:currentPage":function(e){return t.$set(t.tableConfig.pagination,"pageNo",e)},"update:current-page":function(e){return t.$set(t.tableConfig.pagination,"pageNo",e)},"update:pageSize":function(e){return t.$set(t.tableConfig.pagination,"pageSize",e)},"update:page-size":function(e){return t.$set(t.tableConfig.pagination,"pageSize",e)},changePage:t.changePage}}),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:t.clickCancel}},[t._v("取消")])],1)],1)},i=[],o=(a("3835"),a("5530")),l=(a("d81d"),a("4fad"),a("b64b"),a("d3b7"),a("4c40")),c=(a("b592"),a("ec41")),s=a("ff51"),r=(a("e8b2"),{name:"numbers-of-instance",components:{},mixins:[],model:{prop:"show",event:"change"},props:{show:Boolean,rowData:{type:Object,default:null}},data:function(){this.$createElement;return{tableConfig:{loading:!1,pagination:{pageNo:1,pageSize:10,totalCount:0},tableData:[],columns:[{prop:"ip",label:"IP",align:"center",width:"120px"},{prop:"port",label:"端口",align:"center",width:"100px"},{prop:"status",label:"状态",align:"center",width:"100px",render:function(t,e){var a=e.row;return a.status?t("span",[t("HubaryDot",{attrs:{status:"0"==a.status?"default":"processing"}},["1"===a.status?"启动":"停止"])]):t("span")}},{prop:"time",label:"加载时间",align:"center"},{prop:"loadStatus",label:"加载状态",align:"center"},{prop:"message",label:"加载备注",align:"center"}]}}},computed:{visible:{get:function(){return this.show},set:function(t){this.$emit("change",!1)}}},watch:{},created:function(){},mounted:function(){this.getList()},methods:{getList:function(){var t=this,e=Object(o["a"])(Object(o["a"])({},this.rowData),{},{pageNo:this.tableConfig.pagination.pageNo,pageSize:this.tableConfig.pagination.pageSize});this.tableConfig.loading=!0,Object(l["a"])(e).then((function(e){var a=Object(c["a"])(e,"data.data");t.tableConfig.tableData=Object(s["a"])(a)?a:[],t.tableConfig.pagination.totalCount=Object(c["a"])(e,"data.totalCount",0)})).catch((function(t){})).finally((function(){t.tableConfig.loading=!1}))},changePage:function(t,e){t&&(this.tableConfig.pagination.pageNo=t),e&&(this.tableConfig.pagination.pageNo=1,this.tableConfig.pagination.pageSize=e),this.getList()},clickCancel:function(){this.$emit("change",!1)}}});var u=r,g=a("2877"),p=Object(g["a"])(u,n,i,!1,null,"9634ed9e",null);e["default"]=p.exports},"5dbd":function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=t._self._c;return e("div",{staticClass:"name-container"},[e("search-form",{attrs:{searchConfig:t.searchConfig},on:{onSearch:t.onSearch}}),e("comp-table",{ref:"compTable",attrs:{isDialog:!1,isMultiple:!1,loading:t.loading,"table-model":t.tableModel,operateSlice:3,"search-model":t.searchModel},on:{changePage:t.changePage}}),t.dialogVisible?e("NumbersOfInstance",{attrs:{rowData:t.rowData},model:{value:t.dialogVisible,callback:function(e){t.dialogVisible=e},expression:"dialogVisible"}}):t._e()],1)},i=[],o=a("c7eb"),l=a("1da1"),c=a("5530"),s=a("735f"),r=a("ade3"),u=(a("7db0"),a("d81d"),a("d3b7"),a("c43b")),g=a("4c40"),p=a("934b"),d=a("4145"),f=a("ec41"),b=a("ff51"),h={name:"config-query",components:{NumbersOfInstance:d["default"]},data:function(){var t=this,e=(this.$createElement,this);return{rowData:{},dialogVisible:!1,loading:!1,tableModel:{tableData:[],rowData:[{prop:"envName",label:"环境名称"},{prop:"appName",label:"应用名称"},{prop:"configName",label:"配置名称"},{prop:"unitCode",label:"单元"},{prop:"instanceNum",label:"实例数"},{prop:"actions",label:"操作",render:function(e,a){var n=a.row;return e("el-button",{attrs:{type:"text"},on:{click:function(){return t.clickInstance(n)}}},["查看"])}}],operateWidth:"220px",operateData:[],pagination:{pageNo:1,pageSize:10,total:0}},searchModel:{btnGroup:[]},configOptions:[],appOptions:[],envOptions:[],unitOptions:[],searchConfig:{searchData:{appName:"",appId:"",configName:"",configId:"",envId:"",envName:"",unitCode:""},searchItem:[{prop:"envId",label:"环境",type:"select",get options(){return e.envOptions},change:function(e){var a,n=t.envOptions.find((function(t){return t.value===e}));t.searchConfig.searchData.envName=null!==(a=null===n||void 0===n?void 0:n.label)&&void 0!==a?a:"",e&&t.getConfigOptions(!0)}},{prop:"appId",label:"应用",type:"select",clearable:!0,filterable:!0,get options(){return e.appOptions},change:function(e){var a,n=t.appOptions.find((function(t){return t.value===e}));t.searchConfig.searchData.appName=null!==(a=null===n||void 0===n?void 0:n.label)&&void 0!==a?a:"",e&&t.getConfigOptions(!0)}},Object(r["a"])(Object(s["a"])("get",Object(r["a"])(Object(r["a"])({prop:"configId",label:"配置",clearable:!0,type:"select"},"clearable",!0),"filterable",!0),"options",(function(){return e.configOptions})),"change",(function(e){var a,n=t.configOptions.find((function(t){return t.value===e}));t.searchConfig.searchData.configName=null!==(a=null===n||void 0===n?void 0:n.label)&&void 0!==a?a:""})),{type:"select",label:"单元",prop:"unitCode",clearable:!0,filterable:!0,get options(){return e.unitOptions}}],rules:{appId:[{required:!0,message:"请选择应用",trigger:"change"}],envId:[{required:!0,trigger:"change",message:"请选择环境"}],configId:[{required:!1,message:"请选择配置",trigger:"change"}]}}}},created:function(){this.getOptions(),this.getConfigOptions(),this.getUnit()},mounted:function(){},methods:{onSearch:function(t){t?(this.getConfigOptions(),this.tableModel.tableData=[],this.tableModel.pagination.total=0):(this.tableModel.pagination.pageNo=1,this.getList())},getList:function(){var t=this,e=Object(c["a"])({pageNo:this.tableModel.pagination.pageNo,pageSize:this.tableModel.pagination.pageSize},this.searchConfig.searchData);this.loading=!0,Object(g["b"])(e).then((function(e){var a,n=Object(f["a"])(e,"data.data");t.tableModel.tableData=Object(b["a"])(n)?n:[],t.tableModel.pagination.total=(null===e||void 0===e||null===(a=e.data)||void 0===a?void 0:a.totalCount)||0})).catch((function(t){})).finally((function(){t.loading=!1}))},getConfigOptions:function(t){var e=this,a={appId:"",envId:""},n=this.searchConfig.searchData,i=n.appId,o=n.envId;t&&i&&o&&(a.appId=i,a.envId=o),Object(u["d"])(a).then((function(t){e.configOptions=Object(b["a"])(null===t||void 0===t?void 0:t.data)?t.data:[]})).catch((function(t){}))},getOptions:function(){var t=this;Object(u["f"])().then((function(e){t.envOptions=Object(b["a"])(null===e||void 0===e?void 0:e.data)?e.data:[]})).catch((function(t){})),Object(u["e"])().then((function(e){t.appOptions=Object(b["a"])(null===e||void 0===e?void 0:e.data)?e.data:[]})).catch((function(t){}))},getUnit:function(){var t=this;return Object(l["a"])(Object(o["a"])().mark((function e(){var a,n;return Object(o["a"])().wrap((function(e){while(1)switch(e.prev=e.next){case 0:return a=[],e.prev=1,e.next=4,Object(p["c"])({});case 4:n=e.sent,200==n.code&&Object(b["a"])(n.data)&&(a=n.data.map((function(t){return{label:t.unitCode,value:t.unitCode,id:t.id}}))),e.next=11;break;case 8:e.prev=8,e.t0=e["catch"](1);case 11:t.unitOptions=a;case 12:case"end":return e.stop()}}),e,null,[[1,8]])})))()},changePage:function(t,e){t&&(this.tableModel.pagination.pageNo=t),e&&(this.tableModel.pagination.pageNo=1,this.tableModel.pagination.pageSize=e),this.getList()},clickInstance:function(t){this.rowData=t,this.dialogVisible=!0}}},v=h,C=a("2877"),m=Object(C["a"])(v,n,i,!1,null,"e4ade7d8",null);e["default"]=m.exports},b592:function(t){t.exports=JSON.parse("{}")},c13c:function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=t._self._c;return e("el-dialog-limit",{attrs:{title:"配置追踪",visible:t.visible,size:"medium","append-to-body":!0,"close-on-click-modal":!1},on:{close:t.clickCancel}},[e("tableView",{attrs:{loading:t.tableConfig.loading,operateObj:t.tableConfig.operate,columns:t.tableConfig.columns,tableData:t.tableConfig.tableData,isPage:t.tableConfig.isPage,isOrder:!1,total:t.tableConfig.pagination.totalCount,rowKey:"key",isExpand:!0},scopedSlots:t._u([{key:"expand",fn:function(a){var n=a.row;return[Array.isArray(n.annotationTraceEntityList)?e("div",{staticClass:"pl-20"},[e("table",{staticClass:"custom-table w-100"},[e("thead",[e("tr",{staticClass:"p-5"},[e("th",[t._v("客户端字段名")]),e("th",[t._v("字段所属类")])])]),e("tbody",t._l(n.annotationTraceEntityList,(function(a,n){return e("tr",{key:n},[e("td",{staticClass:"p-5"},[t._v(t._s(a.filedName))]),e("td",{staticClass:"p-5"},[t._v(t._s(a.className))])])})),0)])]):e("div",{staticStyle:{"text-align":"center"}},[t._v("暂无数据")])]}}])}),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:t.clickCancel}},[t._v("取消")])],1)],1)},i=[],o=a("070e"),l=a("ff51"),c=a("7b40"),s={name:"add-edit",components:{},mixins:[],model:{prop:"show",event:"change"},props:{show:Boolean,rowData:{type:Object,default:function(){return{}}},refresh:{type:Function,required:!0,default:function(){}}},data:function(){return{tableConfig:{loading:!1,isPage:!1,pagination:{pageNo:1,pageSize:10,totalCount:0},tableData:[],columns:[{prop:"key",label:" 配置属性名"},{prop:"value",label:"配置属性值"}]}}},computed:{visible:{get:function(){return this.show},set:function(t){this.$emit("change",!1)}},isEdit:function(){var t;return Boolean(null===(t=this.rowData)||void 0===t?void 0:t.id)}},watch:{},created:function(){},mounted:function(){this.getDetail()},methods:{getDetail:function(){var t=this,e=Object(o["a"])(this.rowData,["appCode","envCode","configName","configType","keyTrace"]);Object(c["b"])(e).then((function(e){t.tableConfig.tableData=Object(l["a"])(null===e||void 0===e?void 0:e.data)?e.data:[]})).catch((function(t){}))},clickCancel:function(){this.$emit("change",!1)}}},r=s,u=(a("2e3f"),a("2877")),g=Object(u["a"])(r,n,i,!1,null,"2415f520",null);e["default"]=g.exports}}]);