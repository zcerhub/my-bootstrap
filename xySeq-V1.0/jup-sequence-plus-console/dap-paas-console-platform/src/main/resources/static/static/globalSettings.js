(function (global, factory) {
  typeof exports === "object" && typeof module !== "undefined"
    ? (module.exports = factory())
    : typeof define === "function" && define.amd
    ? define(factory)
    : ((global =
        typeof globalThis !== "undefined" ? globalThis : global || self),
      (global.globalSettings = factory()));
})(this, function () {
  "use strict";

  var settings = {
    // 是否打开动态路由功能
    dynamicRouting: true,
    // 是否打开按钮级权限控制功能
    btnPermControl: false,
    // 是否删除无权限按钮
    btnPermRemove: true,
    // 是否展示菜单图标
    menuIconControl: true,
    // 是否进行图标合并,来自接口的图标进行覆盖
    mergeIcon: true,
  };

  Object.freeze(settings);
  return settings;
});
