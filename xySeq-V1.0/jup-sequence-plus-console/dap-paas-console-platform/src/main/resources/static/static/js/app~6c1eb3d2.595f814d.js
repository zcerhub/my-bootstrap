(window["jsonpPaas4"]=window["jsonpPaas4"]||[]).push([["app~6c1eb3d2"],{"0c37":function(e,t,i){var a=i("6d8b"),n="--\x3e",o=function(e){return e.get("autoCurveness")||null},r=function(e,t){var i=o(e),n=20,r=[];if("number"===typeof i)n=i;else if(a.isArray(i))return void(e.__curvenessList=i);t>n&&(n=t);var s=n%2?n+2:n+3;r=[];for(var l=0;l<s;l++)r.push((l%2?l+1:l)/10*(l%2?-1:1));e.__curvenessList=r},s=function(e,t,i){var a=[e.id,e.dataIndex].join("."),o=[t.id,t.dataIndex].join(".");return[i.uid,a,o].join(n)},l=function(e){var t=e.split(n);return[t[0],t[2],t[1]].join(n)},c=function(e,t){var i=s(e.node1,e.node2,t);return t.__edgeMap[i]},u=function(e,t){var i=h(s(e.node1,e.node2,t),t),a=h(s(e.node2,e.node1,t),t);return i+a},h=function(e,t){var i=t.__edgeMap;return i[e]?i[e].length:0};function d(e){o(e)&&(e.__curvenessList=[],e.__edgeMap={},r(e))}function f(e,t,i,a){if(o(i)){var n=s(e,t,i),r=i.__edgeMap,c=r[l(n)];r[n]&&!c?r[n].isForward=!0:c&&r[n]&&(c.isForward=!0,r[n].isForward=!1),r[n]=r[n]||[],r[n].push(a)}}function p(e,t,i,n){var d=o(t),f=a.isArray(d);if(!d)return null;var p=c(e,t);if(!p)return null;for(var m=-1,g=0;g<p.length;g++)if(p[g]===i){m=g;break}var v=u(e,t);r(t,v),e.lineStyle=e.lineStyle||{};var y=s(e.node1,e.node2,t),_=t.__curvenessList,S=f||v%2?0:1;if(p.isForward)return _[S+m];var b=l(y),I=h(b,t),x=_[m+I+S];return n?f?d&&0===d[0]?(I+S)%2?x:-x:((I%2?0:1)+S)%2?x:-x:(I+S)%2?x:-x:_[m+I+S]}t.initCurvenessList=d,t.createEdgeMapForCurveness=f,t.getCurvenessForEdge=p},"0fd3":function(e,t,i){var a=i("2306"),n=i("7e5b"),o=i("6d8b"),r=i("a15a"),s=r.createSymbol,l=i("401b"),c=i("4a3f");function u(e,t,i){a.Group.call(this),this.add(this.createLine(e,t,i)),this._updateEffectSymbol(e,t)}var h=u.prototype;h.createLine=function(e,t,i){return new n(e,t,i)},h._updateEffectSymbol=function(e,t){var i=e.getItemModel(t),a=i.getModel("effect"),n=a.get("symbolSize"),r=a.get("symbol");o.isArray(n)||(n=[n,n]);var l=a.get("color")||e.getItemVisual(t,"color"),c=this.childAt(1);this._symbolType!==r&&(this.remove(c),c=s(r,-.5,-.5,1,1,l),c.z2=100,c.culling=!0,this.add(c)),c&&(c.setStyle("shadowColor",l),c.setStyle(a.getItemStyle(["color"])),c.attr("scale",n),c.setColor(l),c.attr("scale",n),this._symbolType=r,this._symbolScale=n,this._updateEffectAnimation(e,a,t))},h._updateEffectAnimation=function(e,t,i){var a=this.childAt(1);if(a){var n=this,r=e.getItemLayout(i),s=1e3*t.get("period"),l=t.get("loop"),c=t.get("constantSpeed"),u=o.retrieve(t.get("delay"),(function(t){return t/e.count()*s/3})),h="function"===typeof u;if(a.ignore=!0,this.updateAnimationPoints(a,r),c>0&&(s=this.getLineLength(a)/c*1e3),s!==this._period||l!==this._loop){a.stopAnimation();var d=u;h&&(d=u(i)),a.__t>0&&(d=-s*a.__t),a.__t=0;var f=a.animate("",l).when(s,{__t:1}).delay(d).during((function(){n.updateSymbolPosition(a)}));l||f.done((function(){n.remove(a)})),f.start()}this._period=s,this._loop=l}},h.getLineLength=function(e){return l.dist(e.__p1,e.__cp1)+l.dist(e.__cp1,e.__p2)},h.updateAnimationPoints=function(e,t){e.__p1=t[0],e.__p2=t[1],e.__cp1=t[2]||[(t[0][0]+t[1][0])/2,(t[0][1]+t[1][1])/2]},h.updateData=function(e,t,i){this.childAt(0).updateData(e,t,i),this._updateEffectSymbol(e,t)},h.updateSymbolPosition=function(e){var t=e.__p1,i=e.__p2,a=e.__cp1,n=e.__t,o=e.position,r=[o[0],o[1]],s=c.quadraticAt,u=c.quadraticDerivativeAt;o[0]=s(t[0],a[0],i[0],n),o[1]=s(t[1],a[1],i[1],n);var h=u(t[0],a[0],i[0],n),d=u(t[1],a[1],i[1],n);if(e.rotation=-Math.atan2(d,h)-Math.PI/2,"line"===this._symbolType||"rect"===this._symbolType||"roundRect"===this._symbolType)if(void 0!==e.__lastT&&e.__lastT<e.__t){var f=1.05*l.dist(r,o);e.attr("scale",[e.scale[0],f]),1===n&&(o[0]=r[0]+(o[0]-r[0])/2,o[1]=r[1]+(o[1]-r[1])/2)}else if(1===e.__lastT){f=2*l.dist(t,o);e.attr("scale",[e.scale[0],f])}else e.attr("scale",this._symbolScale);e.__lastT=e.__t,e.ignore=!1},h.updateLayout=function(e,t){this.childAt(0).updateLayout(e,t);var i=e.getItemModel(t).getModel("effect");this._updateEffectAnimation(e,i,t)},o.inherits(u,a.Group);var d=u;e.exports=d},1418:function(e,t,i){var a=i("6d8b"),n=i("a15a"),o=n.createSymbol,r=i("2306"),s=i("3842"),l=s.parsePercent,c=i("c775"),u=c.getDefaultLabel;function h(e,t,i){r.Group.call(this),this.updateData(e,t,i)}var d=h.prototype,f=h.getSymbolSize=function(e,t){var i=e.getItemVisual(t,"symbolSize");return i instanceof Array?i.slice():[+i,+i]};function p(e){return[e[0]/2,e[1]/2]}function m(e,t){this.parent.drift(e,t)}d._createSymbol=function(e,t,i,a,n){this.removeAll();var r=t.getItemVisual(i,"color"),s=o(e,-1,-1,2,2,r,n);s.attr({z2:100,culling:!0,scale:p(a)}),s.drift=m,this._symbolType=e,this.add(s)},d.stopSymbolAnimation=function(e){this.childAt(0).stopAnimation(e)},d.getSymbolPath=function(){return this.childAt(0)},d.getScale=function(){return this.childAt(0).scale},d.highlight=function(){this.childAt(0).trigger("emphasis")},d.downplay=function(){this.childAt(0).trigger("normal")},d.setZ=function(e,t){var i=this.childAt(0);i.zlevel=e,i.z=t},d.setDraggable=function(e){var t=this.childAt(0);t.draggable=e,t.cursor=e?"move":t.cursor},d.updateData=function(e,t,i){this.silent=!1;var a=e.getItemVisual(t,"symbol")||"circle",n=e.hostModel,o=f(e,t),s=a!==this._symbolType;if(s){var l=e.getItemVisual(t,"symbolKeepAspect");this._createSymbol(a,e,t,o,l)}else{var c=this.childAt(0);c.silent=!1,r.updateProps(c,{scale:p(o)},n,t)}if(this._updateCommon(e,t,o,i),s){c=this.childAt(0);var u=i&&i.fadeIn,h={scale:c.scale.slice()};u&&(h.style={opacity:c.style.opacity}),c.scale=[0,0],u&&(c.style.opacity=0),r.initProps(c,h,n,t)}this._seriesModel=n};var g=["itemStyle"],v=["emphasis","itemStyle"],y=["label"],_=["emphasis","label"];function S(e,t){if(!this.incremental&&!this.useHoverLayer)if("emphasis"===t){var i=this.__symbolOriginalScale,a=i[1]/i[0],n={scale:[Math.max(1.1*i[0],i[0]+3),Math.max(1.1*i[1],i[1]+3*a)]};this.animateTo(n,400,"elasticOut")}else"normal"===t&&this.animateTo({scale:this.__symbolOriginalScale},400,"elasticOut")}d._updateCommon=function(e,t,i,n){var o=this.childAt(0),s=e.hostModel,c=e.getItemVisual(t,"color");"image"!==o.type?o.useStyle({strokeNoScale:!0}):o.setStyle({opacity:1,shadowBlur:null,shadowOffsetX:null,shadowOffsetY:null,shadowColor:null});var h=n&&n.itemStyle,d=n&&n.hoverItemStyle,f=n&&n.symbolOffset,m=n&&n.labelModel,b=n&&n.hoverLabelModel,I=n&&n.hoverAnimation,x=n&&n.cursorStyle;if(!n||e.hasItemOption){var A=n&&n.itemModel?n.itemModel:e.getItemModel(t);h=A.getModel(g).getItemStyle(["color"]),d=A.getModel(v).getItemStyle(),f=A.getShallow("symbolOffset"),m=A.getModel(y),b=A.getModel(_),I=A.getShallow("hoverAnimation"),x=A.getShallow("cursor")}else d=a.extend({},d);var M=o.style,w=e.getItemVisual(t,"symbolRotate");o.attr("rotation",(w||0)*Math.PI/180||0),f&&o.attr("position",[l(f[0],i[0]),l(f[1],i[1])]),x&&o.attr("cursor",x),o.setColor(c,n&&n.symbolInnerColor),o.setStyle(h);var C=e.getItemVisual(t,"opacity");null!=C&&(M.opacity=C);var D=e.getItemVisual(t,"liftZ"),L=o.__z2Origin;null!=D?null==L&&(o.__z2Origin=o.z2,o.z2+=D):null!=L&&(o.z2=L,o.__z2Origin=null);var P=n&&n.useNameLabel;function O(t,i){return P?e.getName(t):u(e,t)}r.setLabelStyle(M,d,m,b,{labelFetcher:s,labelDataIndex:t,defaultText:O,isRectText:!0,autoColor:c}),o.__symbolOriginalScale=p(i),o.hoverStyle=d,o.highDownOnUpdate=I&&s.isAnimationEnabled()?S:null,r.setHoverStyle(o)},d.fadeOut=function(e,t){var i=this.childAt(0);this.silent=i.silent=!0,(!t||!t.keepLabel)&&(i.style.text=null),r.updateProps(i,{style:{opacity:0},scale:[0,0]},this._seriesModel,this.dataIndex,e)},a.inherits(h,r.Group);var b=h;e.exports=b},"237f":function(e,t,i){var a=i("6d8b"),n=i("6179"),o=i("7368"),r=i("31d9"),s=i("b1d4"),l=i("2039"),c=i("3301");function u(e,t,i,u,h){for(var d=new o(u),f=0;f<e.length;f++)d.addNode(a.retrieve(e[f].id,e[f].name,f),f);var p=[],m=[],g=0;for(f=0;f<t.length;f++){var v=t[f],y=v.source,_=v.target;d.addEdge(y,_,g)&&(m.push(v),p.push(a.retrieve(v.id,y+" > "+_)),g++)}var S,b=i.get("coordinateSystem");if("cartesian2d"===b||"polar"===b)S=c(e,i);else{var I=l.get(b),x=I&&"view"!==I.type&&I.dimensions||[];a.indexOf(x,"value")<0&&x.concat(["value"]);var A=s(e,{coordDimensions:x});S=new n(A,i),S.initData(e)}var M=new n(["value"],i);return M.initData(m,p),h&&h(S,M),r({mainData:S,struct:d,structAttr:"graph",datas:{node:S,edge:M},datasAttr:{node:"data",edge:"edgeData"}}),d.update(),d}e.exports=u},3301:function(e,t,i){var a=i("6d8b"),n=i("6179"),o=i("b1d4"),r=i("93d0"),s=r.SOURCE_FORMAT_ORIGINAL,l=i("2f45"),c=l.getDimensionTypeByAxis,u=i("e0d3"),h=u.getDataItemValue,d=i("2039"),f=i("8b7f"),p=f.getCoordSysInfoBySeries,m=i("ec6f"),g=i("ee1a"),v=g.enableDataStack,y=i("0f99"),_=y.makeSeriesEncodeForAxisCoordSys;function S(e,t,i){i=i||{},m.isInstance(e)||(e=m.seriesDataToSource(e));var r,s=t.get("coordinateSystem"),l=d.get(s),u=p(t);u&&(r=a.map(u.coordSysDims,(function(e){var t={name:e},i=u.axisMap.get(e);if(i){var a=i.get("type");t.type=c(a)}return t}))),r||(r=l&&(l.getDimensionsInfo?l.getDimensionsInfo():l.dimensions.slice())||["x","y"]);var h,f,g=o(e,{coordDimensions:r,generateCoord:i.generateCoord,encodeDefaulter:i.useEncodeDefaulter?a.curry(_,r,t):null});u&&a.each(g,(function(e,t){var i=e.coordDim,a=u.categoryAxisMap.get(i);a&&(null==h&&(h=t),e.ordinalMeta=a.getOrdinalMeta()),null!=e.otherDims.itemName&&(f=!0)})),f||null==h||(g[h].otherDims.itemName=0);var y=v(t,g),S=new n(g,t);S.setCalculationInfo(y);var I=null!=h&&b(e)?function(e,t,i,a){return a===h?i:this.defaultDimValueGetter(e,t,i,a)}:null;return S.hasItemOption=!1,S.initData(e,null,I),S}function b(e){if(e.sourceFormat===s){var t=I(e.data||[]);return null!=t&&!a.isArray(h(t))}}function I(e){var t=0;while(t<e.length&&null==e[t])t++;return e[t]}var x=S;e.exports=x},3970:function(e,t,i){var a=i("4f85"),n=i("3301"),o=i("2039"),r=a.extend({type:"series.heatmap",getInitialData:function(e,t){return n(this.getSource(),this,{generateCoord:"value"})},preventIncremental:function(){var e=o.get(this.get("coordinateSystem"));if(e&&e.dimensions)return"lng"===e.dimensions[0]&&"lat"===e.dimensions[1]},defaultOption:{coordinateSystem:"cartesian2d",zlevel:0,z:2,geoIndex:0,blurSize:30,pointSize:20,maxOpacity:1,minOpacity:0}});e.exports=r},4527:function(e,t,i){var a=i("2306"),n=i("6d8b");function o(e,t,i){a.Group.call(this),this._createPolyline(e,t,i)}var r=o.prototype;r._createPolyline=function(e,t,i){var n=e.getItemLayout(t),o=new a.Polyline({shape:{points:n}});this.add(o),this._updateCommonStl(e,t,i)},r.updateData=function(e,t,i){var n=e.hostModel,o=this.childAt(0),r={shape:{points:e.getItemLayout(t)}};a.updateProps(o,r,n,t),this._updateCommonStl(e,t,i)},r._updateCommonStl=function(e,t,i){var o=this.childAt(0),r=e.getItemModel(t),s=e.getItemVisual(t,"color"),l=i&&i.lineStyle,c=i&&i.hoverLineStyle;i&&!e.hasItemOption||(l=r.getModel("lineStyle").getLineStyle(),c=r.getModel("emphasis.lineStyle").getLineStyle()),o.useStyle(n.defaults({strokeNoScale:!0,fill:"none",stroke:s},l)),o.hoverStyle=c,a.setHoverStyle(this)},r.updateLayout=function(e,t){var i=this.childAt(0);i.setShape("points",e.getItemLayout(t))},n.inherits(o,a.Group);var s=o;e.exports=s},"480e":function(e,t,i){var a=i("4e08"),n=(a.__DEV__,i("3eba")),o=i("2306"),r=i("cd84"),s=i("6d8b");function l(e,t,i){var a=e[1]-e[0];t=s.map(t,(function(t){return{interval:[(t.interval[0]-e[0])/a,(t.interval[1]-e[0])/a]}}));var n=t.length,o=0;return function(e){for(var a=o;a<n;a++){var r=t[a].interval;if(r[0]<=e&&e<=r[1]){o=a;break}}if(a===n)for(a=o-1;a>=0;a--){r=t[a].interval;if(r[0]<=e&&e<=r[1]){o=a;break}}return a>=0&&a<n&&i[a]}}function c(e,t){var i=e[1]-e[0];return t=[(t[0]-e[0])/i,(t[1]-e[0])/i],function(e){return e>=t[0]&&e<=t[1]}}function u(e){var t=e.dimensions;return"lng"===t[0]&&"lat"===t[1]}var h=n.extendChartView({type:"heatmap",render:function(e,t,i){var a;t.eachComponent("visualMap",(function(t){t.eachTargetSeries((function(i){i===e&&(a=t)}))})),this.group.removeAll(),this._incrementalDisplayable=null;var n=e.coordinateSystem;"cartesian2d"===n.type||"calendar"===n.type?this._renderOnCartesianAndCalendar(e,i,0,e.getData().count()):u(n)&&this._renderOnGeo(n,e,a,i)},incrementalPrepareRender:function(e,t,i){this.group.removeAll()},incrementalRender:function(e,t,i,a){var n=t.coordinateSystem;n&&this._renderOnCartesianAndCalendar(t,a,e.start,e.end,!0)},_renderOnCartesianAndCalendar:function(e,t,i,a,n){var r,l,c=e.coordinateSystem;if("cartesian2d"===c.type){var u=c.getAxis("x"),h=c.getAxis("y");r=u.getBandWidth(),l=h.getBandWidth()}for(var d=this.group,f=e.getData(),p="itemStyle",m="emphasis.itemStyle",g="label",v="emphasis.label",y=e.getModel(p).getItemStyle(["color"]),_=e.getModel(m).getItemStyle(),S=e.getModel(g),b=e.getModel(v),I=c.type,x="cartesian2d"===I?[f.mapDimension("x"),f.mapDimension("y"),f.mapDimension("value")]:[f.mapDimension("time"),f.mapDimension("value")],A=i;A<a;A++){var M;if("cartesian2d"===I){if(isNaN(f.get(x[2],A)))continue;var w=c.dataToPoint([f.get(x[0],A),f.get(x[1],A)]);M=new o.Rect({shape:{x:Math.floor(Math.round(w[0])-r/2),y:Math.floor(Math.round(w[1])-l/2),width:Math.ceil(r),height:Math.ceil(l)},style:{fill:f.getItemVisual(A,"color"),opacity:f.getItemVisual(A,"opacity")}})}else{if(isNaN(f.get(x[1],A)))continue;M=new o.Rect({z2:1,shape:c.dataToRect([f.get(x[0],A)]).contentShape,style:{fill:f.getItemVisual(A,"color"),opacity:f.getItemVisual(A,"opacity")}})}var C=f.getItemModel(A);f.hasItemOption&&(y=C.getModel(p).getItemStyle(["color"]),_=C.getModel(m).getItemStyle(),S=C.getModel(g),b=C.getModel(v));var D=e.getRawValue(A),L="-";D&&null!=D[2]&&(L=D[2]),o.setLabelStyle(y,_,S,b,{labelFetcher:e,labelDataIndex:A,defaultText:L,isRectText:!0}),M.setStyle(y),o.setHoverStyle(M,f.hasItemOption?_:s.extend({},_)),M.incremental=n,n&&(M.useHoverLayer=!0),d.add(M),f.setItemGraphicEl(A,M)}},_renderOnGeo:function(e,t,i,a){var n=i.targetVisuals.inRange,s=i.targetVisuals.outOfRange,u=t.getData(),h=this._hmLayer||this._hmLayer||new r;h.blurSize=t.get("blurSize"),h.pointSize=t.get("pointSize"),h.minOpacity=t.get("minOpacity"),h.maxOpacity=t.get("maxOpacity");var d=e.getViewRect().clone(),f=e.getRoamTransform();d.applyTransform(f);var p=Math.max(d.x,0),m=Math.max(d.y,0),g=Math.min(d.width+d.x,a.getWidth()),v=Math.min(d.height+d.y,a.getHeight()),y=g-p,_=v-m,S=[u.mapDimension("lng"),u.mapDimension("lat"),u.mapDimension("value")],b=u.mapArray(S,(function(t,i,a){var n=e.dataToPoint([t,i]);return n[0]-=p,n[1]-=m,n.push(a),n})),I=i.getExtent(),x="visualMap.continuous"===i.type?c(I,i.option.range):l(I,i.getPieceList(),i.option.selected);h.update(b,y,_,n.color.getNormalizer(),{inRange:n.color.getColorMapper(),outOfRange:s.color.getColorMapper()},x);var A=new o.Image({style:{width:y,height:_,x:p,y:m,image:h.canvas},silent:!0});this.group.add(A)},dispose:function(){}});e.exports=h},"55ac":function(e,t,i){var a=i("6d8b");function n(e,t,i){if(e&&a.indexOf(t,e.type)>=0){var n=i.getData().tree.root,o=e.targetNode;if("string"===typeof o&&(o=n.getNodeById(o)),o&&n.contains(o))return{node:o};var r=e.targetNodeId;if(null!=r&&(o=n.getNodeById(r)))return{node:o}}}function o(e){var t=[];while(e)e=e.parentNode,e&&t.push(e);return t.reverse()}function r(e,t){var i=o(e);return a.indexOf(i,t)>=0}function s(e,t){var i=[];while(e){var a=e.dataIndex;i.push({name:e.name,dataIndex:a,value:t.getRawValue(a)}),e=e.parentNode}return i.reverse(),i}t.retrieveTargetInfo=n,t.getPathToRoot=o,t.aboveViewRoot=r,t.wrapTreePathInfo=s},"5ce2":function(e,t,i){i("3970"),i("480e")},"6a4c":function(e,t,i){var a=i("4527"),n=i("6d8b"),o=i("0fd3"),r=i("401b");function s(e,t,i){o.call(this,e,t,i),this._lastFrame=0,this._lastFramePercent=0}var l=s.prototype;l.createLine=function(e,t,i){return new a(e,t,i)},l.updateAnimationPoints=function(e,t){this._points=t;for(var i=[0],a=0,n=1;n<t.length;n++){var o=t[n-1],s=t[n];a+=r.dist(o,s),i.push(a)}if(0!==a){for(n=0;n<i.length;n++)i[n]/=a;this._offsets=i,this._length=a}},l.getLineLength=function(e){return this._length},l.updateSymbolPosition=function(e){var t=e.__t,i=this._points,a=this._offsets,n=i.length;if(a){var o=this._lastFrame;if(t<this._lastFramePercent){var s=Math.min(o+1,n-1);for(l=s;l>=0;l--)if(a[l]<=t)break;l=Math.min(l,n-2)}else{for(var l=o;l<n;l++)if(a[l]>t)break;l=Math.min(l-1,n-2)}r.lerp(e.position,i[l],i[l+1],(t-a[l])/(a[l+1]-a[l]));var c=i[l+1][0]-i[l][0],u=i[l+1][1]-i[l][1];e.rotation=-Math.atan2(u,c)-Math.PI/2,this._lastFrame=l,this._lastFramePercent=t,e.ignore=!1}},n.inherits(s,o);var c=s;e.exports=c},"73ca":function(e,t,i){var a=i("2306"),n=i("7e5b");function o(e){this._ctor=e||n,this.group=new a.Group}var r=o.prototype;function s(e,t,i,a){var n=t.getItemLayout(i);if(d(n)){var o=new e._ctor(t,i,a);t.setItemGraphicEl(i,o),e.group.add(o)}}function l(e,t,i,a,n,o){var r=t.getItemGraphicEl(a);d(i.getItemLayout(n))?(r?r.updateData(i,n,o):r=new e._ctor(i,n,o),i.setItemGraphicEl(n,r),e.group.add(r)):e.group.remove(r)}function c(e){return e.animators&&e.animators.length>0}function u(e){var t=e.hostModel;return{lineStyle:t.getModel("lineStyle").getLineStyle(),hoverLineStyle:t.getModel("emphasis.lineStyle").getLineStyle(),labelModel:t.getModel("label"),hoverLabelModel:t.getModel("emphasis.label")}}function h(e){return isNaN(e[0])||isNaN(e[1])}function d(e){return!h(e[0])&&!h(e[1])}r.isPersistent=function(){return!0},r.updateData=function(e){var t=this,i=t.group,a=t._lineData;t._lineData=e,a||i.removeAll();var n=u(e);e.diff(a).add((function(i){s(t,e,i,n)})).update((function(i,o){l(t,a,e,o,i,n)})).remove((function(e){i.remove(a.getItemGraphicEl(e))})).execute()},r.updateLayout=function(){var e=this._lineData;e&&e.eachItemGraphicEl((function(t,i){t.updateLayout(e,i)}),this)},r.incrementalPrepareUpdate=function(e){this._seriesScope=u(e),this._lineData=null,this.group.removeAll()},r.incrementalUpdate=function(e,t){function i(e){e.isGroup||c(e)||(e.incremental=e.useHoverLayer=!0)}for(var a=e.start;a<e.end;a++){var n=t.getItemLayout(a);if(d(n)){var o=new this._ctor(t,a,this._seriesScope);o.traverse(i),this.group.add(o),t.setItemGraphicEl(a,o)}}},r.remove=function(){this._clearIncremental(),this._incremental=null,this.group.removeAll()},r._clearIncremental=function(){var e=this._incremental;e&&e.clearDisplaybles()};var f=o;e.exports=f},"7e5b":function(e,t,i){var a=i("6d8b"),n=i("401b"),o=i("a15a"),r=i("7f91"),s=i("2306"),l=i("3842"),c=l.round,u=["fromSymbol","toSymbol"];function h(e){return"_"+e+"Type"}function d(e,t,i){var n=t.getItemVisual(i,e);if(n&&"none"!==n){var r=t.getItemVisual(i,"color"),s=t.getItemVisual(i,e+"Size"),l=t.getItemVisual(i,e+"Rotate");a.isArray(s)||(s=[s,s]);var c=o.createSymbol(n,-s[0]/2,-s[1]/2,s[0],s[1],r);return c.__specifiedRotation=null==l||isNaN(l)?void 0:+l*Math.PI/180||0,c.name=e,c}}function f(e){var t=new r({name:"line",subPixelOptimize:!0});return p(t.shape,e),t}function p(e,t){e.x1=t[0][0],e.y1=t[0][1],e.x2=t[1][0],e.y2=t[1][1],e.percent=1;var i=t[2];i?(e.cpx1=i[0],e.cpy1=i[1]):(e.cpx1=NaN,e.cpy1=NaN)}function m(){var e=this,t=e.childOfName("fromSymbol"),i=e.childOfName("toSymbol"),a=e.childOfName("label");if(t||i||!a.ignore){var o=1,r=this.parent;while(r)r.scale&&(o/=r.scale[0]),r=r.parent;var s=e.childOfName("line");if(this.__dirty||s.__dirty){var l=s.shape.percent,c=s.pointAt(0),u=s.pointAt(l),h=n.sub([],u,c);if(n.normalize(h,h),t){t.attr("position",c);var d=t.__specifiedRotation;if(null==d){var f=s.tangentAt(0);t.attr("rotation",Math.PI/2-Math.atan2(f[1],f[0]))}else t.attr("rotation",d);t.attr("scale",[o*l,o*l])}if(i){i.attr("position",u);d=i.__specifiedRotation;if(null==d){f=s.tangentAt(1);i.attr("rotation",-Math.PI/2-Math.atan2(f[1],f[0]))}else i.attr("rotation",d);i.attr("scale",[o*l,o*l])}if(!a.ignore){var p,m,g,v;a.attr("position",u);var y=a.__labelDistance,_=y[0]*o,S=y[1]*o,b=l/2,I=(f=s.tangentAt(b),[f[1],-f[0]]),x=s.pointAt(b);I[1]>0&&(I[0]=-I[0],I[1]=-I[1]);var A,M=f[0]<0?-1:1;if("start"!==a.__position&&"end"!==a.__position){var w=-Math.atan2(f[1],f[0]);u[0]<c[0]&&(w=Math.PI+w),a.attr("rotation",w)}switch(a.__position){case"insideStartTop":case"insideMiddleTop":case"insideEndTop":case"middle":A=-S,g="bottom";break;case"insideStartBottom":case"insideMiddleBottom":case"insideEndBottom":A=S,g="top";break;default:A=0,g="middle"}switch(a.__position){case"end":p=[h[0]*_+u[0],h[1]*S+u[1]],m=h[0]>.8?"left":h[0]<-.8?"right":"center",g=h[1]>.8?"top":h[1]<-.8?"bottom":"middle";break;case"start":p=[-h[0]*_+c[0],-h[1]*S+c[1]],m=h[0]>.8?"right":h[0]<-.8?"left":"center",g=h[1]>.8?"bottom":h[1]<-.8?"top":"middle";break;case"insideStartTop":case"insideStart":case"insideStartBottom":p=[_*M+c[0],c[1]+A],m=f[0]<0?"right":"left",v=[-_*M,-A];break;case"insideMiddleTop":case"insideMiddle":case"insideMiddleBottom":case"middle":p=[x[0],x[1]+A],m="center",v=[0,-A];break;case"insideEndTop":case"insideEnd":case"insideEndBottom":p=[-_*M+u[0],u[1]+A],m=f[0]>=0?"right":"left",v=[_*M,-A];break}a.attr({style:{textVerticalAlign:a.__verticalAlign||g,textAlign:a.__textAlign||m},position:p,scale:[o,o],origin:v})}}}}function g(e,t,i){s.Group.call(this),this._createLine(e,t,i)}var v=g.prototype;v.beforeUpdate=m,v._createLine=function(e,t,i){var n=e.hostModel,o=e.getItemLayout(t),r=f(o);r.shape.percent=0,s.initProps(r,{shape:{percent:1}},n,t),this.add(r);var l=new s.Text({name:"label",lineLabelOriginalOpacity:1});this.add(l),a.each(u,(function(i){var a=d(i,e,t);this.add(a),this[h(i)]=e.getItemVisual(t,i)}),this),this._updateCommonStl(e,t,i)},v.updateData=function(e,t,i){var n=e.hostModel,o=this.childOfName("line"),r=e.getItemLayout(t),l={shape:{}};p(l.shape,r),s.updateProps(o,l,n,t),a.each(u,(function(i){var a=e.getItemVisual(t,i),n=h(i);if(this[n]!==a){this.remove(this.childOfName(i));var o=d(i,e,t);this.add(o)}this[n]=a}),this),this._updateCommonStl(e,t,i)},v._updateCommonStl=function(e,t,i){var n=e.hostModel,o=this.childOfName("line"),r=i&&i.lineStyle,l=i&&i.hoverLineStyle,h=i&&i.labelModel,d=i&&i.hoverLabelModel;if(!i||e.hasItemOption){var f=e.getItemModel(t);r=f.getModel("lineStyle").getLineStyle(),l=f.getModel("emphasis.lineStyle").getLineStyle(),h=f.getModel("label"),d=f.getModel("emphasis.label")}var p=e.getItemVisual(t,"color"),m=a.retrieve3(e.getItemVisual(t,"opacity"),r.opacity,1);o.useStyle(a.defaults({strokeNoScale:!0,fill:"none",stroke:p,opacity:m},r)),o.hoverStyle=l,a.each(u,(function(e){var t=this.childOfName(e);t&&(t.setColor(p),t.setStyle({opacity:m}))}),this);var g,v,y=h.getShallow("show"),_=d.getShallow("show"),S=this.childOfName("label");if((y||_)&&(g=p||"#000",v=n.getFormattedLabel(t,"normal",e.dataType),null==v)){var b=n.getRawValue(t);v=null==b?e.getName(t):isFinite(b)?c(b):b}var I=y?v:null,x=_?a.retrieve2(n.getFormattedLabel(t,"emphasis",e.dataType),v):null,A=S.style;if(null!=I||null!=x){s.setTextStyle(S.style,h,{text:I},{autoColor:g}),S.__textAlign=A.textAlign,S.__verticalAlign=A.textVerticalAlign,S.__position=h.get("position")||"middle";var M=h.get("distance");a.isArray(M)||(M=[M,M]),S.__labelDistance=M}S.hoverStyle=null!=x?{text:x,textFill:d.getTextColor(!0),fontStyle:d.getShallow("fontStyle"),fontWeight:d.getShallow("fontWeight"),fontSize:d.getShallow("fontSize"),fontFamily:d.getShallow("fontFamily")}:{text:null},S.ignore=!y&&!_,s.setHoverStyle(this)},v.highlight=function(){this.trigger("emphasis")},v.downplay=function(){this.trigger("normal")},v.updateLayout=function(e,t){this.setLinePoints(e.getItemLayout(t))},v.setLinePoints=function(e){var t=this.childOfName("line");p(t.shape,e),t.dirty()},a.inherits(g,s.Group);var y=g;e.exports=y},"7f91":function(e,t,i){var a=i("2306"),n=i("401b"),o=a.Line.prototype,r=a.BezierCurve.prototype;function s(e){return isNaN(+e.cpx1)||isNaN(+e.cpy1)}var l=a.extendShape({type:"ec-line",style:{stroke:"#000",fill:null},shape:{x1:0,y1:0,x2:0,y2:0,percent:1,cpx1:null,cpy1:null},buildPath:function(e,t){this[s(t)?"_buildPathLine":"_buildPathCurve"](e,t)},_buildPathLine:o.buildPath,_buildPathCurve:r.buildPath,pointAt:function(e){return this[s(this.shape)?"_pointAtLine":"_pointAtCurve"](e)},_pointAtLine:o.pointAt,_pointAtCurve:r.pointAt,tangentAt:function(e){var t=this.shape,i=s(t)?[t.x2-t.x1,t.y2-t.y1]:this._tangentAtCurve(e);return n.normalize(i,i)},_tangentAtCurve:r.tangentAt});e.exports=l},a38d:function(e,t,i){var a=i("2306"),n=i("392f"),o=i("9680"),r=i("68ab"),s=a.extendShape({shape:{polyline:!1,curveness:0,segs:[]},buildPath:function(e,t){var i=t.segs,a=t.curveness;if(t.polyline)for(var n=0;n<i.length;){var o=i[n++];if(o>0){e.moveTo(i[n++],i[n++]);for(var r=1;r<o;r++)e.lineTo(i[n++],i[n++])}}else for(n=0;n<i.length;){var s=i[n++],l=i[n++],c=i[n++],u=i[n++];if(e.moveTo(s,l),a>0){var h=(s+c)/2-(l-u)*a,d=(l+u)/2-(c-s)*a;e.quadraticCurveTo(h,d,c,u)}else e.lineTo(c,u)}},findDataIndex:function(e,t){var i=this.shape,a=i.segs,n=i.curveness;if(i.polyline)for(var s=0,l=0;l<a.length;){var c=a[l++];if(c>0)for(var u=a[l++],h=a[l++],d=1;d<c;d++){var f=a[l++],p=a[l++];if(o.containStroke(u,h,f,p))return s}s++}else for(s=0,l=0;l<a.length;){u=a[l++],h=a[l++],f=a[l++],p=a[l++];if(n>0){var m=(u+f)/2-(h-p)*n,g=(h+p)/2-(f-u)*n;if(r.containStroke(u,h,m,g,f,p))return s}else if(o.containStroke(u,h,f,p))return s;s++}return-1}});function l(){this.group=new a.Group}var c=l.prototype;c.isPersistent=function(){return!this._incremental},c.updateData=function(e){this.group.removeAll();var t=new s({rectHover:!0,cursor:"default"});t.setShape({segs:e.getLayout("linesPoints")}),this._setCommon(t,e),this.group.add(t),this._incremental=null},c.incrementalPrepareUpdate=function(e){this.group.removeAll(),this._clearIncremental(),e.count()>5e5?(this._incremental||(this._incremental=new n({silent:!0})),this.group.add(this._incremental)):this._incremental=null},c.incrementalUpdate=function(e,t){var i=new s;i.setShape({segs:t.getLayout("linesPoints")}),this._setCommon(i,t,!!this._incremental),this._incremental?this._incremental.addDisplayable(i,!0):(i.rectHover=!0,i.cursor="default",i.__startIndex=e.start,this.group.add(i))},c.remove=function(){this._clearIncremental(),this._incremental=null,this.group.removeAll()},c._setCommon=function(e,t,i){var a=t.hostModel;e.setShape({polyline:a.get("polyline"),curveness:a.get("lineStyle.curveness")}),e.useStyle(a.getModel("lineStyle").getLineStyle()),e.style.strokeNoScale=!0;var n=t.getVisual("color");n&&e.setStyle("stroke",n),e.setStyle("fill"),i||(e.seriesIndex=a.seriesIndex,e.on("mousemove",(function(t){e.dataIndex=null;var i=e.findDataIndex(t.offsetX,t.offsetY);i>0&&(e.dataIndex=i+e.__startIndex)})))},c._clearIncremental=function(){var e=this._incremental;e&&e.clearDisplaybles()};var u=l;e.exports=u},b0af:function(e,t,i){var a=i("2306"),n=i("3842"),o=n.round;function r(e,t,i){var n=e.getArea(),o=e.getBaseAxis().isHorizontal(),r=n.x,s=n.y,l=n.width,c=n.height,u=i.get("lineStyle.width")||2;r-=u/2,s-=u/2,l+=u,c+=u,r=Math.floor(r),l=Math.round(l);var h=new a.Rect({shape:{x:r,y:s,width:l,height:c}});return t&&(h.shape[o?"width":"height"]=0,a.initProps(h,{shape:{width:l,height:c}},i)),h}function s(e,t,i){var n=e.getArea(),r=new a.Sector({shape:{cx:o(e.cx,1),cy:o(e.cy,1),r0:o(n.r0,1),r:o(n.r,1),startAngle:n.startAngle,endAngle:n.endAngle,clockwise:n.clockwise}});return t&&(r.shape.endAngle=n.startAngle,a.initProps(r,{shape:{endAngle:n.endAngle}},i)),r}function l(e,t,i){return e?"polar"===e.type?s(e,t,i):"cartesian2d"===e.type?r(e,t,i):null:null}t.createGridClipPath=r,t.createPolarClipPath=s,t.createClipPath=l},c775:function(e,t,i){var a=i("2b17"),n=a.retrieveRawValue;function o(e,t){var i=e.mapDimension("defaultedLabel",!0),a=i.length;if(1===a)return n(e,t,i[0]);if(a){for(var o=[],r=0;r<i.length;r++){var s=n(e,t,i[r]);o.push(s)}return o.join(" ")}}t.getDefaultLabel=o},c8ef:function(e,t,i){var a=i("6d8b"),n=i("a15a"),o=n.createSymbol,r=i("2306"),s=r.Group,l=i("3842"),c=l.parsePercent,u=i("1418"),h=3;function d(e){return a.isArray(e)||(e=[+e,+e]),e}function f(e,t){var i=t.rippleEffectColor||t.color;e.eachChild((function(e){e.attr({z:t.z,zlevel:t.zlevel,style:{stroke:"stroke"===t.brushType?i:null,fill:"fill"===t.brushType?i:null}})}))}function p(e,t){s.call(this);var i=new u(e,t),a=new s;this.add(i),this.add(a),a.beforeUpdate=function(){this.attr(i.getScale())},this.updateData(e,t)}var m=p.prototype;m.stopEffectAnimation=function(){this.childAt(1).removeAll()},m.startEffectAnimation=function(e){for(var t=e.symbolType,i=e.color,a=this.childAt(1),n=0;n<h;n++){var r=o(t,-1,-1,2,2,i);r.attr({style:{strokeNoScale:!0},z2:99,silent:!0,scale:[.5,.5]});var s=-n/h*e.period+e.effectOffset;r.animate("",!0).when(e.period,{scale:[e.rippleScale/2,e.rippleScale/2]}).delay(s).start(),r.animateStyle(!0).when(e.period,{opacity:0}).delay(s).start(),a.add(r)}f(a,e)},m.updateEffectAnimation=function(e){for(var t=this._effectCfg,i=this.childAt(1),a=["symbolType","period","rippleScale"],n=0;n<a.length;n++){var o=a[n];if(t[o]!==e[o])return this.stopEffectAnimation(),void this.startEffectAnimation(e)}f(i,e)},m.highlight=function(){this.trigger("emphasis")},m.downplay=function(){this.trigger("normal")},m.updateData=function(e,t){var i=e.hostModel;this.childAt(0).updateData(e,t);var a=this.childAt(1),n=e.getItemModel(t),o=e.getItemVisual(t,"symbol"),r=d(e.getItemVisual(t,"symbolSize")),s=e.getItemVisual(t,"color");a.attr("scale",r),a.traverse((function(e){e.attr({fill:s})}));var l=n.getShallow("symbolOffset");if(l){var u=a.position;u[0]=c(l[0],r[0]),u[1]=c(l[1],r[1])}var h=e.getItemVisual(t,"symbolRotate");a.rotation=(h||0)*Math.PI/180||0;var f={};if(f.showEffectOn=i.get("showEffectOn"),f.rippleScale=n.get("rippleEffect.scale"),f.brushType=n.get("rippleEffect.brushType"),f.period=1e3*n.get("rippleEffect.period"),f.effectOffset=t/e.count(),f.z=n.getShallow("z")||0,f.zlevel=n.getShallow("zlevel")||0,f.symbolType=o,f.color=s,f.rippleEffectColor=n.get("rippleEffect.color"),this.off("mouseover").off("mouseout").off("emphasis").off("normal"),"render"===f.showEffectOn)this._effectCfg?this.updateEffectAnimation(f):this.startEffectAnimation(f),this._effectCfg=f;else{this._effectCfg=null,this.stopEffectAnimation();var p=this.childAt(0),m=function(){p.highlight(),"render"!==f.showEffectOn&&this.startEffectAnimation(f)},g=function(){p.downplay(),"render"!==f.showEffectOn&&this.stopEffectAnimation()};this.on("mouseover",m,this).on("mouseout",g,this).on("emphasis",m,this).on("normal",g,this)}this._effectCfg=f},m.fadeOut=function(e){this.off("mouseover").off("mouseout").off("emphasis").off("normal"),e&&e()},a.inherits(p,s);var g=p;e.exports=g},c965:function(e,t,i){var a=i("2306"),n=i("a15a"),o=n.createSymbol,r=i("392f"),s=4,l=a.extendShape({shape:{points:null},symbolProxy:null,softClipShape:null,buildPath:function(e,t){var i=t.points,a=t.size,n=this.symbolProxy,o=n.shape,r=e.getContext?e.getContext():e,l=r&&a[0]<s;if(!l)for(var c=0;c<i.length;){var u=i[c++],h=i[c++];isNaN(u)||isNaN(h)||(this.softClipShape&&!this.softClipShape.contain(u,h)||(o.x=u-a[0]/2,o.y=h-a[1]/2,o.width=a[0],o.height=a[1],n.buildPath(e,o,!0)))}},afterBrush:function(e){var t=this.shape,i=t.points,a=t.size,n=a[0]<s;if(n){this.setTransform(e);for(var o=0;o<i.length;){var r=i[o++],l=i[o++];isNaN(r)||isNaN(l)||(this.softClipShape&&!this.softClipShape.contain(r,l)||e.fillRect(r-a[0]/2,l-a[1]/2,a[0],a[1]))}this.restoreTransform(e)}},findDataIndex:function(e,t){for(var i=this.shape,a=i.points,n=i.size,o=Math.max(n[0],4),r=Math.max(n[1],4),s=a.length/2-1;s>=0;s--){var l=2*s,c=a[l]-o/2,u=a[l+1]-r/2;if(e>=c&&t>=u&&e<=c+o&&t<=u+r)return s}return-1}});function c(){this.group=new a.Group}var u=c.prototype;u.isPersistent=function(){return!this._incremental},u.updateData=function(e,t){this.group.removeAll();var i=new l({rectHover:!0,cursor:"default"});i.setShape({points:e.getLayout("symbolPoints")}),this._setCommon(i,e,!1,t),this.group.add(i),this._incremental=null},u.updateLayout=function(e){if(!this._incremental){var t=e.getLayout("symbolPoints");this.group.eachChild((function(e){if(null!=e.startIndex){var i=2*(e.endIndex-e.startIndex),a=4*e.startIndex*2;t=new Float32Array(t.buffer,a,i)}e.setShape("points",t)}))}},u.incrementalPrepareUpdate=function(e){this.group.removeAll(),this._clearIncremental(),e.count()>2e6?(this._incremental||(this._incremental=new r({silent:!0})),this.group.add(this._incremental)):this._incremental=null},u.incrementalUpdate=function(e,t,i){var a;this._incremental?(a=new l,this._incremental.addDisplayable(a,!0)):(a=new l({rectHover:!0,cursor:"default",startIndex:e.start,endIndex:e.end}),a.incremental=!0,this.group.add(a)),a.setShape({points:t.getLayout("symbolPoints")}),this._setCommon(a,t,!!this._incremental,i)},u._setCommon=function(e,t,i,a){var n=t.hostModel;a=a||{};var r=t.getVisual("symbolSize");e.setShape("size",r instanceof Array?r:[r,r]),e.softClipShape=a.clipShape||null,e.symbolProxy=o(t.getVisual("symbol"),0,0,0,0),e.setColor=e.symbolProxy.setColor;var l=e.shape.size[0]<s;e.useStyle(n.getModel("itemStyle").getItemStyle(l?["color","shadowBlur","shadowColor"]:["color"]));var c=t.getVisual("color");c&&e.setColor(c),i||(e.seriesIndex=n.seriesIndex,e.on("mousemove",(function(t){e.dataIndex=null;var i=e.findDataIndex(t.offsetX,t.offsetY);i>=0&&(e.dataIndex=i+(e.startIndex||0))})))},u.remove=function(){this._clearIncremental(),this._incremental=null,this.group.removeAll()},u._clearIncremental=function(){var e=this._incremental;e&&e.clearDisplaybles()};var h=c;e.exports=h},cccd:function(e,t,i){var a=i("e0d3"),n=a.makeInner;function o(){var e=n();return function(t){var i=e(t),a=t.pipelineContext,n=i.large,o=i.progressiveRender,r=i.large=a&&a.large,s=i.progressiveRender=a&&a.progressiveRender;return!!(n^r||o^s)&&"reset"}}e.exports=o},cd84:function(e,t,i){var a=i("6d8b"),n=256;function o(){var e=a.createCanvas();this.canvas=e,this.blurSize=30,this.pointSize=20,this.maxOpacity=1,this.minOpacity=0,this._gradientPixels={}}o.prototype={update:function(e,t,i,a,o,r){var s=this._getBrush(),l=this._getGradient(e,o,"inRange"),c=this._getGradient(e,o,"outOfRange"),u=this.pointSize+this.blurSize,h=this.canvas,d=h.getContext("2d"),f=e.length;h.width=t,h.height=i;for(var p=0;p<f;++p){var m=e[p],g=m[0],v=m[1],y=m[2],_=a(y);d.globalAlpha=_,d.drawImage(s,g-u,v-u)}if(!h.width||!h.height)return h;var S=d.getImageData(0,0,h.width,h.height),b=S.data,I=0,x=b.length,A=this.minOpacity,M=this.maxOpacity,w=M-A;while(I<x){_=b[I+3]/256;var C=4*Math.floor(_*(n-1));if(_>0){var D=r(_)?l:c;_>0&&(_=_*w+A),b[I++]=D[C],b[I++]=D[C+1],b[I++]=D[C+2],b[I++]=D[C+3]*_*256}else I+=4}return d.putImageData(S,0,0),h},_getBrush:function(){var e=this._brushCanvas||(this._brushCanvas=a.createCanvas()),t=this.pointSize+this.blurSize,i=2*t;e.width=i,e.height=i;var n=e.getContext("2d");return n.clearRect(0,0,i,i),n.shadowOffsetX=i,n.shadowBlur=this.blurSize,n.shadowColor="#000",n.beginPath(),n.arc(-t,t,this.pointSize,0,2*Math.PI,!0),n.closePath(),n.fill(),e},_getGradient:function(e,t,i){for(var a=this._gradientPixels,n=a[i]||(a[i]=new Uint8ClampedArray(1024)),o=[0,0,0,0],r=0,s=0;s<256;s++)t[i](s/255,!0,o),n[r++]=o[0],n[r++]=o[1],n[r++]=o[2],n[r++]=o[3];return n}};var r=o;e.exports=r},d2a5:function(e,t,i){var a=i("3eba");a.registerAction({type:"focusNodeAdjacency",event:"focusNodeAdjacency",update:"series:focusNodeAdjacency"},(function(){})),a.registerAction({type:"unfocusNodeAdjacency",event:"unfocusNodeAdjacency",update:"series:unfocusNodeAdjacency"},(function(){}))},e468:function(e,t,i){var a=i("e46b"),n=i("6d8b"),o=i("2f45"),r=o.getDimensionTypeByAxis,s=i("0f99"),l=s.makeSeriesEncodeForAxisCoordSys,c={_baseAxisDim:null,getInitialData:function(e,t){var i,o,s=t.getComponent("xAxis",this.get("xAxisIndex")),c=t.getComponent("yAxis",this.get("yAxisIndex")),u=s.get("type"),h=c.get("type");"category"===u?(e.layout="horizontal",i=s.getOrdinalMeta(),o=!0):"category"===h?(e.layout="vertical",i=c.getOrdinalMeta(),o=!0):e.layout=e.layout||"horizontal";var d=["x","y"],f="horizontal"===e.layout?0:1,p=this._baseAxisDim=d[f],m=d[1-f],g=[s,c],v=g[f].get("type"),y=g[1-f].get("type"),_=e.data;if(_&&o){var S=[];n.each(_,(function(e,t){var i;e.value&&n.isArray(e.value)?(i=e.value.slice(),e.value.unshift(t)):n.isArray(e)?(i=e.slice(),e.unshift(t)):i=e,S.push(i)})),e.data=S}var b=this.defaultValueDimensions,I=[{name:p,type:r(v),ordinalMeta:i,otherDims:{tooltip:!1,itemName:0},dimsDef:["base"]},{name:m,type:r(y),dimsDef:b.slice()}];return a(this,{coordDimensions:I,dimensionsCount:b.length+1,encodeDefaulter:n.curry(l,I,this)})},getBaseAxis:function(){var e=this._baseAxisDim;return this.ecModel.getComponent(e+"Axis",this.get(e+"AxisIndex")).axis}};t.seriesModelMixin=c},e46b:function(e,t,i){var a=i("b1d4"),n=i("6179"),o=i("6d8b"),r=o.extend,s=o.isArray;function l(e,t,i){t=s(t)&&{coordDimensions:t}||r({},t);var o=e.getSource(),l=a(o,t),c=new n(l,e);return c.initData(o,i),c}e.exports=l},f706:function(e,t,i){var a=i("2306"),n=i("1418"),o=i("6d8b"),r=o.isObject;function s(e){this.group=new a.Group,this._symbolCtor=e||n}var l=s.prototype;function c(e,t,i,a){return t&&!isNaN(t[0])&&!isNaN(t[1])&&!(a.isIgnore&&a.isIgnore(i))&&!(a.clipShape&&!a.clipShape.contain(t[0],t[1]))&&"none"!==e.getItemVisual(i,"symbol")}function u(e){return null==e||r(e)||(e={isIgnore:e}),e||{}}function h(e){var t=e.hostModel;return{itemStyle:t.getModel("itemStyle").getItemStyle(["color"]),hoverItemStyle:t.getModel("emphasis.itemStyle").getItemStyle(),symbolRotate:t.get("symbolRotate"),symbolOffset:t.get("symbolOffset"),hoverAnimation:t.get("hoverAnimation"),labelModel:t.getModel("label"),hoverLabelModel:t.getModel("emphasis.label"),cursorStyle:t.get("cursor")}}l.updateData=function(e,t){t=u(t);var i=this.group,n=e.hostModel,o=this._data,r=this._symbolCtor,s=h(e);o||i.removeAll(),e.diff(o).add((function(a){var n=e.getItemLayout(a);if(c(e,n,a,t)){var o=new r(e,a,s);o.attr("position",n),e.setItemGraphicEl(a,o),i.add(o)}})).update((function(l,u){var h=o.getItemGraphicEl(u),d=e.getItemLayout(l);c(e,d,l,t)?(h?(h.updateData(e,l,s),a.updateProps(h,{position:d},n)):(h=new r(e,l),h.attr("position",d)),i.add(h),e.setItemGraphicEl(l,h)):i.remove(h)})).remove((function(e){var t=o.getItemGraphicEl(e);t&&t.fadeOut((function(){i.remove(t)}))})).execute(),this._data=e},l.isPersistent=function(){return!0},l.updateLayout=function(){var e=this._data;e&&e.eachItemGraphicEl((function(t,i){var a=e.getItemLayout(i);t.attr("position",a)}))},l.incrementalPrepareUpdate=function(e){this._seriesScope=h(e),this._data=null,this.group.removeAll()},l.incrementalUpdate=function(e,t,i){function a(e){e.isGroup||(e.incremental=e.useHoverLayer=!0)}i=u(i);for(var n=e.start;n<e.end;n++){var o=t.getItemLayout(n);if(c(t,o,n,i)){var r=new this._symbolCtor(t,n,this._seriesScope);r.traverse(a),r.attr("position",o),this.group.add(r),t.setItemGraphicEl(n,r)}}},l.remove=function(e){var t=this.group,i=this._data;i&&e?i.eachItemGraphicEl((function(e){e.fadeOut((function(){t.remove(e)}))})):t.removeAll()};var d=s;e.exports=d}}]);