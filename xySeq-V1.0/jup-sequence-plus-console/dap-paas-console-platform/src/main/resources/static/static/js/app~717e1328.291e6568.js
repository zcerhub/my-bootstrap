(window["jsonpPaas4"]=window["jsonpPaas4"]||[]).push([["app~717e1328"],{"0e0f":function(e,t,a){var n=a("5f14"),o=a("6d8b");function i(e,t){e.eachSeriesByType("sankey",(function(e){var t=e.getGraph(),a=t.nodes;if(a.length){var i=1/0,r=-1/0;o.each(a,(function(e){var t=e.getLayout().value;t<i&&(i=t),t>r&&(r=t)})),o.each(a,(function(t){var a=new n({type:"color",mappingMethod:"linear",dataExtent:[i,r],visual:e.get("color")}),o=a.mapValueToVisual(t.getLayout().value),u=t.getModel().get("itemStyle.color");null!=u?t.setVisual("color",u):t.setVisual("color",o)}))}}))}e.exports=i},"15af":function(e,t,a){var n=a("3eba");a("cb69"),a("abff");var o=a("7f96"),i=a("87c3");a("01ed"),n.registerVisual(o("scatter","circle")),n.registerLayout(i("scatter"))},"311a":function(e,t,a){var n=a("3eba");a("d01c"),a("5b69"),a("bdc0");var o=a("81ac"),i=a("0e0f");n.registerLayout(o),n.registerVisual(i)},"5b69":function(e,t,a){var n=a("2306"),o=a("3eba"),i=a("6d8b"),r=["itemStyle","opacity"],u=["emphasis","itemStyle","opacity"],s=["lineStyle","opacity"],d=["emphasis","lineStyle","opacity"];function l(e,t){return e.getVisual("opacity")||e.getModel().get(t)}function c(e,t,a){var n=e.getGraphicEl(),o=l(e,t);null!=a&&(null==o&&(o=1),o*=a),n.downplay&&n.downplay(),n.traverse((function(e){"group"!==e.type&&e.setStyle("opacity",o)}))}function g(e,t){var a=l(e,t),n=e.getGraphicEl();n.traverse((function(e){"group"!==e.type&&e.setStyle("opacity",a)})),n.highlight&&n.highlight()}var h=n.extendShape({shape:{x1:0,y1:0,x2:0,y2:0,cpx1:0,cpy1:0,cpx2:0,cpy2:0,extent:0,orient:""},buildPath:function(e,t){var a=t.extent;e.moveTo(t.x1,t.y1),e.bezierCurveTo(t.cpx1,t.cpy1,t.cpx2,t.cpy2,t.x2,t.y2),"vertical"===t.orient?(e.lineTo(t.x2+a,t.y2),e.bezierCurveTo(t.cpx2+a,t.cpy2,t.cpx1+a,t.cpy1,t.x1+a,t.y1)):(e.lineTo(t.x2,t.y2+a),e.bezierCurveTo(t.cpx2,t.cpy2+a,t.cpx1,t.cpy1+a,t.x1,t.y1+a)),e.closePath()},highlight:function(){this.trigger("emphasis")},downplay:function(){this.trigger("normal")}}),y=o.extendChartView({type:"sankey",_model:null,_focusAdjacencyDisabled:!1,render:function(e,t,a){var o=this,i=e.getGraph(),r=this.group,u=e.layoutInfo,s=u.width,d=u.height,l=e.getData(),c=e.getData("edge"),g=e.get("orient");this._model=e,r.removeAll(),r.attr("position",[u.x,u.y]),i.eachEdge((function(t){var a=new h;a.dataIndex=t.dataIndex,a.seriesIndex=e.seriesIndex,a.dataType="edge";var o,i,u,l,y,f,p,v,m=t.getModel("lineStyle"),x=m.get("curveness"),L=t.node1.getLayout(),b=t.node1.getModel(),D=b.get("localX"),I=b.get("localY"),w=t.node2.getLayout(),_=t.node2.getModel(),E=_.get("localX"),A=_.get("localY"),S=t.getLayout();switch(a.shape.extent=Math.max(1,S.dy),a.shape.orient=g,"vertical"===g?(o=(null!=D?D*s:L.x)+S.sy,i=(null!=I?I*d:L.y)+L.dy,u=(null!=E?E*s:w.x)+S.ty,l=null!=A?A*d:w.y,y=o,f=i*(1-x)+l*x,p=u,v=i*x+l*(1-x)):(o=(null!=D?D*s:L.x)+L.dx,i=(null!=I?I*d:L.y)+S.sy,u=null!=E?E*s:w.x,l=(null!=A?A*d:w.y)+S.ty,y=o*(1-x)+u*x,f=i,p=o*x+u*(1-x),v=l),a.setShape({x1:o,y1:i,x2:u,y2:l,cpx1:y,cpy1:f,cpx2:p,cpy2:v}),a.setStyle(m.getItemStyle()),a.style.fill){case"source":a.style.fill=t.node1.getVisual("color");break;case"target":a.style.fill=t.node2.getVisual("color");break}n.setHoverStyle(a,t.getModel("emphasis.lineStyle").getItemStyle()),r.add(a),c.setItemGraphicEl(t.dataIndex,a)})),i.eachNode((function(t){var a=t.getLayout(),o=t.getModel(),i=o.get("localX"),u=o.get("localY"),c=o.getModel("label"),g=o.getModel("emphasis.label"),h=new n.Rect({shape:{x:null!=i?i*s:a.x,y:null!=u?u*d:a.y,width:a.dx,height:a.dy},style:o.getModel("itemStyle").getItemStyle()}),y=t.getModel("emphasis.itemStyle").getItemStyle();n.setLabelStyle(h.style,y,c,g,{labelFetcher:e,labelDataIndex:t.dataIndex,defaultText:t.id,isRectText:!0}),h.setStyle("fill",t.getVisual("color")),n.setHoverStyle(h,y),r.add(h),l.setItemGraphicEl(t.dataIndex,h),h.dataType="node"})),l.eachItemGraphicEl((function(t,n){var i=l.getItemModel(n);i.get("draggable")&&(t.drift=function(t,i){o._focusAdjacencyDisabled=!0,this.shape.x+=t,this.shape.y+=i,this.dirty(),a.dispatchAction({type:"dragNode",seriesId:e.id,dataIndex:l.getRawIndex(n),localX:this.shape.x/s,localY:this.shape.y/d})},t.ondragend=function(){o._focusAdjacencyDisabled=!1},t.draggable=!0,t.cursor="move"),t.highlight=function(){this.trigger("emphasis")},t.downplay=function(){this.trigger("normal")},t.focusNodeAdjHandler&&t.off("mouseover",t.focusNodeAdjHandler),t.unfocusNodeAdjHandler&&t.off("mouseout",t.unfocusNodeAdjHandler),i.get("focusNodeAdjacency")&&(t.on("mouseover",t.focusNodeAdjHandler=function(){o._focusAdjacencyDisabled||(o._clearTimer(),a.dispatchAction({type:"focusNodeAdjacency",seriesId:e.id,dataIndex:t.dataIndex}))}),t.on("mouseout",t.unfocusNodeAdjHandler=function(){o._focusAdjacencyDisabled||o._dispatchUnfocus(a)}))})),c.eachItemGraphicEl((function(t,n){var i=c.getItemModel(n);t.focusNodeAdjHandler&&t.off("mouseover",t.focusNodeAdjHandler),t.unfocusNodeAdjHandler&&t.off("mouseout",t.unfocusNodeAdjHandler),i.get("focusNodeAdjacency")&&(t.on("mouseover",t.focusNodeAdjHandler=function(){o._focusAdjacencyDisabled||(o._clearTimer(),a.dispatchAction({type:"focusNodeAdjacency",seriesId:e.id,edgeDataIndex:t.dataIndex}))}),t.on("mouseout",t.unfocusNodeAdjHandler=function(){o._focusAdjacencyDisabled||o._dispatchUnfocus(a)}))})),!this._data&&e.get("animation")&&r.setClipPath(f(r.getBoundingRect(),e,(function(){r.removeClipPath()}))),this._data=e.getData()},dispose:function(){this._clearTimer()},_dispatchUnfocus:function(e){var t=this;this._clearTimer(),this._unfocusDelayTimer=setTimeout((function(){t._unfocusDelayTimer=null,e.dispatchAction({type:"unfocusNodeAdjacency",seriesId:t._model.id})}),500)},_clearTimer:function(){this._unfocusDelayTimer&&(clearTimeout(this._unfocusDelayTimer),this._unfocusDelayTimer=null)},focusNodeAdjacency:function(e,t,a,n){var o=e.getData(),l=o.graph,h=n.dataIndex,y=o.getItemModel(h),f=n.edgeDataIndex;if(null!=h||null!=f){var p=l.getNodeByIndex(h),v=l.getEdgeByIndex(f);if(l.eachNode((function(e){c(e,r,.1)})),l.eachEdge((function(e){c(e,s,.1)})),p){g(p,u);var m=y.get("focusNodeAdjacency");"outEdges"===m?i.each(p.outEdges,(function(e){e.dataIndex<0||(g(e,d),g(e.node2,u))})):"inEdges"===m?i.each(p.inEdges,(function(e){e.dataIndex<0||(g(e,d),g(e.node1,u))})):"allEdges"===m&&i.each(p.edges,(function(e){e.dataIndex<0||(g(e,d),e.node1!==p&&g(e.node1,u),e.node2!==p&&g(e.node2,u))}))}v&&(g(v,d),g(v.node1,u),g(v.node2,u))}},unfocusNodeAdjacency:function(e,t,a,n){var o=e.getGraph();o.eachNode((function(e){c(e,r)})),o.eachEdge((function(e){c(e,s)}))}});function f(e,t,a){var o=new n.Rect({shape:{x:e.x-10,y:e.y-10,width:0,height:e.height+20}});return n.initProps(o,{shape:{width:e.width+20}},t,a),o}e.exports=y},"81ac":function(e,t,a){var n=a("f934"),o=a("6d8b"),i=a("e0d3"),r=i.groupData;function u(e,t,a){e.eachSeriesByType("sankey",(function(e){var a=e.get("nodeWidth"),n=e.get("nodeGap"),i=s(e,t);e.layoutInfo=i;var r=i.width,u=i.height,c=e.getGraph(),g=c.nodes,h=c.edges;l(g);var y=o.filter(g,(function(e){return 0===e.getLayout().value})),f=0!==y.length?0:e.get("layoutIterations"),p=e.get("orient"),v=e.get("nodeAlign");d(g,h,a,n,r,u,f,p,v)}))}function s(e,t){return n.getLayoutRect(e.getBoxLayoutParams(),{width:t.getWidth(),height:t.getHeight()})}function d(e,t,a,n,o,i,r,u,s){c(e,t,a,o,i,u,s),p(e,t,i,o,n,r,u),N(e,u)}function l(e){o.each(e,(function(e){var t=A(e.outEdges,E),a=A(e.inEdges,E),n=e.getValue()||0,o=Math.max(t,a,n);e.setLayout({value:o},!0)}))}function c(e,t,a,n,o,i,r){for(var u=[],s=[],d=[],l=[],c=0,g=0,y=0;y<t.length;y++)u[y]=1;for(y=0;y<e.length;y++)s[y]=e[y].inEdges.length,0===s[y]&&d.push(e[y]);var p=-1;while(d.length){for(var v=0;v<d.length;v++){var m=d[v],x=m.hostGraph.data.getRawDataItem(m.dataIndex),L=null!=x.depth&&x.depth>=0;L&&x.depth>p&&(p=x.depth),m.setLayout({depth:L?x.depth:c},!0),"vertical"===i?m.setLayout({dy:a},!0):m.setLayout({dx:a},!0);for(var b=0;b<m.outEdges.length;b++){var D=m.outEdges[b],I=t.indexOf(D);u[I]=0;var w=D.node2,_=e.indexOf(w);0===--s[_]&&l.indexOf(w)<0&&l.push(w)}}++c,d=l,l=[]}for(y=0;y<u.length;y++)if(1===u[y])throw new Error("Sankey is a DAG, the original data has cycle!");var E=p>c-1?p:c-1;r&&"left"!==r&&h(e,r,i,E);g="vertical"===i?(o-a)/E:(n-a)/E;f(e,g,i)}function g(e){var t=e.hostGraph.data.getRawDataItem(e.dataIndex);return null!=t.depth&&t.depth>=0}function h(e,t,a,n){if("right"===t){var i=[],r=e,u=0;while(r.length){for(var s=0;s<r.length;s++){var d=r[s];d.setLayout({skNodeHeight:u},!0);for(var l=0;l<d.inEdges.length;l++){var c=d.inEdges[l];i.indexOf(c.node1)<0&&i.push(c.node1)}}r=i,i=[],++u}o.each(e,(function(e){g(e)||e.setLayout({depth:Math.max(0,n-e.getLayout().skNodeHeight)},!0)}))}else"justify"===t&&y(e,n)}function y(e,t){o.each(e,(function(e){g(e)||e.outEdges.length||e.setLayout({depth:t},!0)}))}function f(e,t,a){o.each(e,(function(e){var n=e.getLayout().depth*t;"vertical"===a?e.setLayout({y:n},!0):e.setLayout({x:n},!0)}))}function p(e,t,a,n,o,i,r){var u=v(e,r);m(u,t,a,n,o,r),x(u,o,a,n,r);for(var s=1;i>0;i--)s*=.99,L(u,s,r),x(u,o,a,n,r),S(u,s,r),x(u,o,a,n,r)}function v(e,t){var a=[],n="vertical"===t?"y":"x",i=r(e,(function(e){return e.getLayout()[n]}));return i.keys.sort((function(e,t){return e-t})),o.each(i.keys,(function(e){a.push(i.buckets.get(e))})),a}function m(e,t,a,n,i,r){var u=1/0;o.each(e,(function(e){var t=e.length,s=0;o.each(e,(function(e){s+=e.getLayout().value}));var d="vertical"===r?(n-(t-1)*i)/s:(a-(t-1)*i)/s;d<u&&(u=d)})),o.each(e,(function(e){o.each(e,(function(e,t){var a=e.getLayout().value*u;"vertical"===r?(e.setLayout({x:t},!0),e.setLayout({dx:a},!0)):(e.setLayout({y:t},!0),e.setLayout({dy:a},!0))}))})),o.each(t,(function(e){var t=+e.getValue()*u;e.setLayout({dy:t},!0)}))}function x(e,t,a,n,i){var r="vertical"===i?"x":"y";o.each(e,(function(e){var o,u,s;e.sort((function(e,t){return e.getLayout()[r]-t.getLayout()[r]}));for(var d=0,l=e.length,c="vertical"===i?"dx":"dy",g=0;g<l;g++)u=e[g],s=d-u.getLayout()[r],s>0&&(o=u.getLayout()[r]+s,"vertical"===i?u.setLayout({x:o},!0):u.setLayout({y:o},!0)),d=u.getLayout()[r]+u.getLayout()[c]+t;var h="vertical"===i?n:a;if(s=d-t-h,s>0)for(o=u.getLayout()[r]-s,"vertical"===i?u.setLayout({x:o},!0):u.setLayout({y:o},!0),d=o,g=l-2;g>=0;--g)u=e[g],s=u.getLayout()[r]+u.getLayout()[c]+t-d,s>0&&(o=u.getLayout()[r]-s,"vertical"===i?u.setLayout({x:o},!0):u.setLayout({y:o},!0)),d=u.getLayout()[r]}))}function L(e,t,a){o.each(e.slice().reverse(),(function(e){o.each(e,(function(e){if(e.outEdges.length){var n=A(e.outEdges,b,a)/A(e.outEdges,E,a);if(isNaN(n)){var o=e.outEdges.length;n=o?A(e.outEdges,D,a)/o:0}if("vertical"===a){var i=e.getLayout().x+(n-_(e,a))*t;e.setLayout({x:i},!0)}else{var r=e.getLayout().y+(n-_(e,a))*t;e.setLayout({y:r},!0)}}}))}))}function b(e,t){return _(e.node2,t)*e.getValue()}function D(e,t){return _(e.node2,t)}function I(e,t){return _(e.node1,t)*e.getValue()}function w(e,t){return _(e.node1,t)}function _(e,t){return"vertical"===t?e.getLayout().x+e.getLayout().dx/2:e.getLayout().y+e.getLayout().dy/2}function E(e){return e.getValue()}function A(e,t,a){var n=0,o=e.length,i=-1;while(++i<o){var r=+t.call(e,e[i],a);isNaN(r)||(n+=r)}return n}function S(e,t,a){o.each(e,(function(e){o.each(e,(function(e){if(e.inEdges.length){var n=A(e.inEdges,I,a)/A(e.inEdges,E,a);if(isNaN(n)){var o=e.inEdges.length;n=o?A(e.inEdges,w,a)/o:0}if("vertical"===a){var i=e.getLayout().x+(n-_(e,a))*t;e.setLayout({x:i},!0)}else{var r=e.getLayout().y+(n-_(e,a))*t;e.setLayout({y:r},!0)}}}))}))}function N(e,t){var a="vertical"===t?"x":"y";o.each(e,(function(e){e.outEdges.sort((function(e,t){return e.node2.getLayout()[a]-t.node2.getLayout()[a]})),e.inEdges.sort((function(e,t){return e.node1.getLayout()[a]-t.node1.getLayout()[a]}))})),o.each(e,(function(e){var t=0,a=0;o.each(e.outEdges,(function(e){e.setLayout({sy:t},!0),t+=e.getLayout().dy})),o.each(e.inEdges,(function(e){e.setLayout({ty:a},!0),a+=e.getLayout().dy}))}))}e.exports=u},abff:function(e,t,a){var n=a("3eba"),o=a("f706"),i=a("c965"),r=a("87c3");n.extendChartView({type:"scatter",render:function(e,t,a){var n=e.getData(),o=this._updateSymbolDraw(n,e);o.updateData(n,{clipShape:this._getClipShape(e)}),this._finished=!0},incrementalPrepareRender:function(e,t,a){var n=e.getData(),o=this._updateSymbolDraw(n,e);o.incrementalPrepareUpdate(n),this._finished=!1},incrementalRender:function(e,t,a){this._symbolDraw.incrementalUpdate(e,t.getData(),{clipShape:this._getClipShape(t)}),this._finished=e.end===t.getData().count()},updateTransform:function(e,t,a){var n=e.getData();if(this.group.dirty(),!this._finished||n.count()>1e4||!this._symbolDraw.isPersistent())return{update:!0};var o=r().reset(e);o.progress&&o.progress({start:0,end:n.count()},n),this._symbolDraw.updateLayout(n)},_getClipShape:function(e){var t=e.coordinateSystem,a=t&&t.getArea&&t.getArea();return e.get("clip",!0)?a:null},_updateSymbolDraw:function(e,t){var a=this._symbolDraw,n=t.pipelineContext,r=n.large;return a&&r===this._isLargeDraw||(a&&a.remove(),a=this._symbolDraw=r?new i:new o,this._isLargeDraw=r,this.group.removeAll()),this.group.add(a.group),a},remove:function(e,t){this._symbolDraw&&this._symbolDraw.remove(!0),this._symbolDraw=null},dispose:function(){}})},bdc0:function(e,t,a){var n=a("3eba");a("d2a5"),n.registerAction({type:"dragNode",event:"dragnode",update:"update"},(function(e,t){t.eachComponent({mainType:"series",subType:"sankey",query:e},(function(t){t.setNodePosition(e.dataIndex,[e.localX,e.localY])}))}))},cb69:function(e,t,a){var n=a("3301"),o=a("4f85"),i=o.extend({type:"series.scatter",dependencies:["grid","polar","geo","singleAxis","calendar"],getInitialData:function(e,t){return n(this.getSource(),this,{useEncodeDefaulter:!0})},brushSelector:"point",getProgressive:function(){var e=this.option.progressive;return null==e?this.option.large?5e3:this.get("progressive"):e},getProgressiveThreshold:function(){var e=this.option.progressiveThreshold;return null==e?this.option.large?1e4:this.get("progressiveThreshold"):e},defaultOption:{coordinateSystem:"cartesian2d",zlevel:0,z:2,legendHoverLink:!0,hoverAnimation:!0,symbolSize:10,large:!1,largeThreshold:2e3,itemStyle:{opacity:.8},clip:!0}});e.exports=i},d01c:function(e,t,a){var n=a("4f85"),o=a("237f"),i=a("eda2"),r=i.encodeHTML,u=a("4319"),s=a("4e08"),d=(s.__DEV__,n.extend({type:"series.sankey",layoutInfo:null,levelModels:null,getInitialData:function(e,t){for(var a=e.edges||e.links,n=e.data||e.nodes,i=e.levels,r=this.levelModels={},s=0;s<i.length;s++)null!=i[s].depth&&i[s].depth>=0&&(r[i[s].depth]=new u(i[s],this,t));if(n&&a){var d=o(n,a,this,!0,l);return d.data}function l(e,t){e.wrapMethod("getItemModel",(function(e,t){return e.customizeGetParent((function(e){var a=this.parentModel,n=a.getData().getItemLayout(t).depth,o=a.levelModels[n];return o||this.parentModel})),e})),t.wrapMethod("getItemModel",(function(e,t){return e.customizeGetParent((function(e){var a=this.parentModel,n=a.getGraph().getEdgeByIndex(t),o=n.node1.getLayout().depth,i=a.levelModels[o];return i||this.parentModel})),e}))}},setNodePosition:function(e,t){var a=this.option.data[e];a.localX=t[0],a.localY=t[1]},getGraph:function(){return this.getData().graph},getEdgeData:function(){return this.getGraph().edgeData},formatTooltip:function(e,t,a){if("edge"===a){var n=this.getDataParams(e,a),o=n.data,i=o.source+" -- "+o.target;return n.value&&(i+=" : "+n.value),r(i)}if("node"===a){var u=this.getGraph().getNodeByIndex(e),s=u.getLayout().value,l=this.getDataParams(e,a).data.name;if(s)i=l+" : "+s;return r(i)}return d.superCall(this,"formatTooltip",e,t)},optionUpdated:function(){var e=this.option;!0===e.focusNodeAdjacency&&(e.focusNodeAdjacency="allEdges")},getDataParams:function(e,t){var a=d.superCall(this,"getDataParams",e,t);if(null==a.value&&"node"===t){var n=this.getGraph().getNodeByIndex(e),o=n.getLayout().value;a.value=o}return a},defaultOption:{zlevel:0,z:2,coordinateSystem:"view",layout:null,left:"5%",top:"5%",right:"20%",bottom:"5%",orient:"horizontal",nodeWidth:20,nodeGap:8,draggable:!0,focusNodeAdjacency:!1,layoutIterations:32,label:{show:!0,position:"right",color:"#000",fontSize:12},levels:[],nodeAlign:"justify",itemStyle:{borderWidth:1,borderColor:"#333"},lineStyle:{color:"#314656",opacity:.2,curveness:.5},emphasis:{label:{show:!0},lineStyle:{opacity:.5}},animationEasing:"linear",animationDuration:1e3}})),l=d;e.exports=l}}]);