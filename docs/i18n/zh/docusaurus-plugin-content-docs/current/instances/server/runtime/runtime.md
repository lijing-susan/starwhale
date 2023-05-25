---
title: 运行时环境管理
---

## 运行时环境列表

### 运行时环境列表页

运行时环境列表页展示了运行时环境的基本信息，包括：运行时环境名称，运行时环境最新版本别名，运行时环境归属人、创建时间和相关操作。

![image](https://user-images.githubusercontent.com/101299635/236448328-3d77a384-388b-4135-b0b5-8089ce7af589.png)

### 运行时环境历史版本

1 点击 **历史版本** 可查看该运行时环境的所有历史版本。

![image](https://user-images.githubusercontent.com/101299635/236449156-01fed362-a72b-4218-b5fc-672b907f7200.png)

2 运行时环境历史版本列表展示了运行时环境版本基本信息，包括版本ID，版本别名，版本是否被公开分享，版本归属人和相关操作。

![image](https://user-images.githubusercontent.com/101299635/234825710-37a13e18-7df1-471c-b339-445f49435c91.png)

:::tip
如果运行时环境被公开分享，Starwhale所有的用户可见并可用。如果运行时环境不被公开分享，只对有权限的人可见和可用。

## 运行时环境详情

运行时环境版本信息包含3个部分：概览，元数据和文件。

**概览**：展示运行时环境版本的基本信息，包括：运行时环境名称，运行时环境版本名称，运行时环境版本别名，版本是否被公开共享和版本创建时间。

![image](https://user-images.githubusercontent.com/101299635/236450424-4d27a47d-761e-4cff-a24f-ff8b4964aefc.png)

:::tip
如果数据集归属某个公共项目，可以通过打开是否共享数据集版本的**开关**按钮来共享数据集。公开共享的数据集版本对Starwhale全部用户可见可用。了解更多信息，请参考[角色和权限](https://doc.starwhale.ai/zh/docs/concepts/roles-permissions)。 
:::

**元数据**: 运行时环境的元数据，包括制品信息，构建信息，配置信息，依赖信息和环境信息。

![image](https://user-images.githubusercontent.com/101299635/236451479-ba4e330b-650f-41a5-9cd8-66f1ba0a845a.png)

**文件**: 显示 `runtime.swrt` 和大小。

![image](https://user-images.githubusercontent.com/101299635/236451573-4d1b3289-e6c8-4099-9650-bf7c0bf0bf12.png)

### 切换到指定版本

点击版本下拉框，选择需切换的版本

![image](https://user-images.githubusercontent.com/101299635/236452461-c622e1f1-cffd-4d42-92e7-dfb58b1f2f41.png)

## 运行时环境分享

打开**分享**按钮，即可分享，关闭即可停止分享。归属公共项目的分享运行时环境能够被Starwhale所有的公户看到和使用。了解更多信息，请参考[角色和权限](https://doc.starwhale.ai/docs/concepts/roles-permissions)。

![image](https://user-images.githubusercontent.com/101299635/236457794-29b81612-7ea4-407f-a6cc-1b35028d9af7.png)

## 移除或恢复运行时环境

### 移除

点击 **移除**，运行时环境会移至回收站。

:::important
如果确认移除，运行时环境的所有版本都会被移除。
:::

![image](https://user-images.githubusercontent.com/101299635/236459483-254aa2e5-9b2e-4de5-ad45-57ca3b2103ff.png)

### 还原

可以在 **回收站** 还远运行时环境。了解更多信息, 请参考 [如何恢复回收站数据](https://github.com/lijing-susan/starwhale/blob/lijing-docs/docs/docs/instances/server/Trash/Trash.md#to-restore)
