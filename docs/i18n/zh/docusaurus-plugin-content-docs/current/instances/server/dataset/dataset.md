---
title:数据集管理
---

## 数据集列表

### 数据集列表

数据集列表页展示了数据集的基本信息，包括：数据集名称、数据集最新版本别名、数据集所有人、数据集创建时间和数据集操作。

![image](https://user-images.githubusercontent.com/101299635/234795143-2987043e-9dd2-4b73-8ff9-73b83762356e.png)

### 数据集历史版本

1 点击 **历史版本**按钮可查看数据集的全部历史版本。

![image](https://user-images.githubusercontent.com/101299635/234795306-7311c641-ca31-44ae-9c59-a3a66433285a.png)

2 数据集历史版本列表页展示了数据集版本的基本信息，包括：数据集名称，版本名称，版本别名，该版本是否公开分享和版本创建时间。

![image](https://user-images.githubusercontent.com/101299635/234825710-37a13e18-7df1-471c-b339-445f49435c91.png)

:::tip
如果公开分享数据集某版本，则该版本对Starwahle全部用户可见可用；如果没有公开分享，则仅对权限的用户可见和使用。
:::

## 数据集详情

数据集详情包括三部分：概览、元数据和文件。

**概览**：数据集的基本信息，包括：数据集名称，版本名称，版本别名，该版本是否公开共享和版本创建时间。

![image](https://user-images.githubusercontent.com/101299635/234795667-e2331a52-351d-4dcd-a5ad-631596cd2ea9.png)

:::tip
如果数据集归属某个公共项目，可以通过打开是否共享数据集版本的**开关**按钮来共享数据集。公开共享的数据集版本对Starwhale全部用户可见可用。了解更多信息，请参考[角色和权限](https://doc.starwhale.ai/zh/docs/concepts/roles-permissions)

**元数据**：数据集的元数据，包括数据集构建信息，构建时使用的Starwhale版本信息和数据集概述信息。

![image](https://user-images.githubusercontent.com/101299635/234795955-337bd013-782b-48fd-97dc-1af9067ffc1a.png)

**文件**：支持查看包含视频、音频、图片、文本和标注等在内的多种数据。

| 数据类型 | 格式 | 标注类型 |
|---|---|---|
| 图片 |jpg, jpeg, png | 2D Box, 2D Polygon, 2D Polyline, 2D Keypoint, Classification, Segmentation |
| 视频 | MP4 | Classification |
| 音频 | MP3, wav | Classification |
| 文本 | txt | Classification |

- **图片数据**

支持列表视图（默认）和大屏视图
  
  - 列表视图
  
  ![image](https://user-images.githubusercontent.com/101299635/234798959-ba214fb9-bf94-413b-b6b5-81d0d9f5ba40.png)
  
  - 大屏视图

  左侧展示标注信息，可将标注设置为展示或隐藏。
  
  ![image](https://user-images.githubusercontent.com/101299635/234799661-9b33cf77-975f-40be-8f87-55a705848660.png)
  
- **视频数据**

支持列表视图（默认）和大屏视图

  - 列表视图
  
  ![img_v2_9a04dddd-6c39-4b45-8763-075798f1e48g](https://user-images.githubusercontent.com/101299635/234829713-42ca7580-d2b6-4e98-b9d4-92f8c0e2585d.jpg)
  
  - 大屏视图
  
  1 左侧展示标注信息，可将标注设置为展示或隐藏。
  
  2 右下角的按钮支持视频播放/暂停、调整音量、快进和全拼查看。

  ![img_v2_3e961d3c-4d57-42ff-b18a-1d9cf65db64g](https://user-images.githubusercontent.com/101299635/234829798-ea4f6cb2-3c88-43dd-88d9-55e43db3a95c.jpg)
  
 - **音频数据**

支持列表视图（默认）和大屏视图

  - 列表视图
  
  ![image](https://user-images.githubusercontent.com/101299635/234803932-59089931-00c3-4d12-b101-5d4f11191df3.png)
  
  - 大屏视图
  
  1 左侧展示标注信息，可将标注设置为展示或隐藏。
  
  2 右下角的按钮支持视频播放/暂停、调整音量；点击**更多**，可以下载和调整播放速度。

  ![image](https://user-images.githubusercontent.com/101299635/234804126-3f6f76b0-95ba-4bf4-9150-6ee7c9563c54.png)
  
  - **文本数据**

  支持列表视图（默认）和大屏视图
  
  ![image](https://user-images.githubusercontent.com/101299635/234797034-84f2c866-a06a-4552-a292-966d9e8522d5.png)

### 切换指定版本

点击版本下拉框，选择需切换的版本即可

![image](https://user-images.githubusercontent.com/101299635/234826002-98678c2e-469b-402c-a3ed-0fa7af6157f5.png)

#### 筛选和过滤

交互友好得筛选和过滤操作

![image](https://user-images.githubusercontent.com/101299635/234832373-c5dd7d53-584d-4235-9021-519ae1c25123.png)

## 数据集分享

将是否分享数据集开关切换只打开或者关闭状态可快速共享或者停止共享数据集。归属公共项目的分享数据集能够被Starwhale所有的公户看到和使用。了解更多信息，请查看[角色和权限](https://doc.starwhale.ai/docs/concepts/roles-permissions)

![image](https://user-images.githubusercontent.com/101299635/234837932-18a7270f-a1c5-48f6-8aec-5f58458d357c.png)

## 移除或恢复数据集

### 移除

点击 **移除**，数据集会移至回收站.

:::important
如果确定移除，数据集的所有版本都会被移除.
:::

![image](https://user-images.githubusercontent.com/101299635/234845251-f96b7cb5-c2e5-4257-be18-ed89840eb323.png)

### 恢复

可以去 **回收站** 恢复数据集。了解更多信息, 请查看 [如何恢复回收站数据](https://github.com/lijing-susan/starwhale/blob/lijing-docs/docs/docs/instances/server/Trash/Trash.md#to-restore)
