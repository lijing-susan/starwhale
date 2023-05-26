---
title:模型评测
---

## 评测列表

模型评测列表可以看到一个项目中的全部评测。

用户每次使用Starwahle Ui,客户端或者 Python SDK 跑评测后，列表页就会出现一条新的记录（默认按时间倒序排列）。

![image](https://github.com/lijing-susan/starwhale/assets/101299635/0fececdd-3675-4dcf-a2d2-36e52393a5eb)

### 自定义评测列表视图

可以将您的自定义配置保存成为视图，视图对每个项目成员可见。

**保存和另存为视图**

如果您编辑了视图配置，如过滤条件，列等，**另存为** 和 **保存** 按钮会自动出现。 

 - 点击 **保存** 将新配置保存至当前视图。

  ![image](https://user-images.githubusercontent.com/101299635/236804900-d67bc11e-df68-4d13-b7e9-cf35f8ca094e.png)

 - 点击 **另存为**, 输入视图名称，点击**保存** 可另存为新视图.
 
 ![image](https://user-images.githubusercontent.com/101299635/236805810-358e5d68-2009-40bc-a16b-c5b4e12427dd.png)

 ![image](https://user-images.githubusercontent.com/101299635/236806207-1d8f4a30-321a-496a-94fd-4a2cf2d8a4e1.png)

**新增视图**

1 点击自定义视图菜单栏中的 **新增视图** 

![image](https://user-images.githubusercontent.com/101299635/236823774-620a8ec9-0307-436d-a2bd-9dceb0703788.png)

2 输入视图名称，编辑配置项，如过滤条，列和排序，然后点击 **保存**来保存视图。

 ![image](https://user-images.githubusercontent.com/101299635/236806207-1d8f4a30-321a-496a-94fd-4a2cf2d8a4e1.png)

**管理视图**

1 点击自定义视图菜单栏中的 **管理视图** 

![image](https://user-images.githubusercontent.com/101299635/236825459-3edbd540-456e-4e50-86a3-74f6ce038c1f.png)

2 变更默认视图或编辑视图是否可见，点击 **保存** 保存变更

![image](https://user-images.githubusercontent.com/101299635/236826075-79de0048-30b1-4670-9594-66b7645796f7.png)

### 评测对比

通过勾选或者取消勾选每个评测前的复选框来选择要对比的评测

![image](https://github.com/lijing-susan/starwhale/assets/101299635/3ca880ae-7f57-4e54-bd1c-24952f756cd6)

![image](https://github.com/lijing-susan/starwhale/assets/101299635/1a0c81c2-fd2e-411f-9ef2-26b2d37fd77d)

### 筛选和过滤

支持简单筛选和高级筛选

![image](https://github.com/lijing-susan/starwhale/assets/101299635/d14a8399-907e-428c-9a28-be9388d49e3e)

## 评测详情

### 评测概览

评测概览信息包括：评测指标，评测状态，模型及模型版本名称，运行时环境及版本名称，归属人，资源池名称和步骤。

![image](https://github.com/lijing-susan/starwhale/assets/101299635/177621b6-85f1-4eed-b54b-6f4d92713efe)

### 自定义面板

**创建**

- 点击页面底部的 **新增面板** 按钮新建面板

只有“项目所有者”角色可以新增面板。

![image](https://github.com/lijing-susan/starwhale/assets/101299635/a9b85788-8d87-44e9-a9f7-3603c0fe4e13)

- 或者点击**更多**按钮，然后选择在当前面板上方或者当前面板下方新建面板。

![image](https://github.com/lijing-susan/starwhale/assets/101299635/25fdf551-4166-4b73-8240-8aed482d16af)

**重新命名**

点击 **更多** 。然后点击 **重新命名** 输入新名字，可以重新命名面板。

![image](https://github.com/lijing-susan/starwhale/assets/101299635/e16d7d20-2f87-4e83-a507-1724855735de)

**删除面板**

点击 **更多**, 然后点击 **删除**可以删除面板。

![image](https://github.com/lijing-susan/starwhale/assets/101299635/e6d46ac7-d5db-40e0-8573-1a532bfecc49)

**变更面板展示顺序**

将鼠标悬浮到面板中间，点击中间的按钮，然后将面板拖拽至您想调整到的位置。

![image](https://github.com/lijing-susan/starwhale/assets/101299635/ba97aa85-b963-4edd-9b2c-888ff63878d4)

### 自定义图表

**新建图表**

点击 面板右上方**新增图表** 按钮可新增图表。

![image](https://github.com/lijing-susan/starwhale/assets/101299635/489a679e-ea01-4cbf-ad9f-e8aed59231a3)

支持的图表类型: 表格, Roc-Auc 图标和混淆举证。

**编辑图表**

鼠标悬浮在图表上，点击 **配置** 按钮, 然后点击 **编辑图表**可对图表进行编辑。

![image](https://github.com/lijing-susan/starwhale/assets/101299635/7e1291a3-38a7-4d6a-8869-b63037ec4bd3)

**删除图表**

鼠标悬浮在图表上，点击 **配置** 按钮, 然后点击 **删除图表**可将图表删除。

![image](https://github.com/lijing-susan/starwhale/assets/101299635/b15d4113-2774-42ee-b82f-c07cf0910845)

### 评测任务和日志

**任务**

点击 **任务** 页可以看到该评测的所有任务。

![image](https://github.com/lijing-susan/starwhale/assets/101299635/2f580fa4-868e-4d8d-b14a-ea72396fd757)

**日志**

点击 **查看日志** 按钮可查看任务的相关日志。

![image](https://github.com/lijing-susan/starwhale/assets/101299635/f1b896a7-f55b-40c9-a419-f44e0a3575fa)
