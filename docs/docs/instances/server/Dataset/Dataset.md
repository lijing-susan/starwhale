---
title: Dataset in Starwhale
---

## Dataset list

### Dataset display

Starwhale dataset list displays the basic dataset information: Dataset name, the latest dataset version alias, dataset owner, the created time and dataset actions.

![image](https://user-images.githubusercontent.com/101299635/234795143-2987043e-9dd2-4b73-8ff9-73b83762356e.png)

### History versions of a dataset

1 Click **Version History** to see all the history versions of a dataset.

![image](https://user-images.githubusercontent.com/101299635/234795306-7311c641-ca31-44ae-9c59-a3a66433285a.png)

2 Starwhale dataset history version list displays the basic dataset version information: Version name, version alias, whether the version is shared, the created time of the version and dataset version action.

![image](https://user-images.githubusercontent.com/101299635/234825710-37a13e18-7df1-471c-b339-445f49435c91.png)

:::tip
If a dataset version is shared, it is visible to anyone. If it is not shared,  it can only be seen and used by users with permission. 
:::

## Dataset details

Dataset version information consists of three parts: Overview, metadata, and files.

**Overview**: The basic information of a dataset version, including dataset name, dataset version name, dataset version aliases, whether the dataset version is shared, and the created time of a dataset version.

![image](https://user-images.githubusercontent.com/101299635/234795667-e2331a52-351d-4dcd-a5ad-631596cd2ea9.png)

:::tip
Click the switch button to set a dataset to a shared mode if the dataset belongs to a public project. A shared dataset can be seen and used for everyone. See more permission information.(Link to the project) 
:::

**Metadata**: The metadata of the dataset, including the building os information, Starwhale version information, and dataset summarized information.

![image](https://user-images.githubusercontent.com/101299635/234795955-337bd013-782b-48fd-97dc-1af9067ffc1a.png)

**Files**: Support viewing data like videos, images, audio, texts, and annotations.

| Data Types | Format | Annotation Types |
|---|---|---|
| Images |jpg, jpeg, png | 2D Box, 2D Polygon, 2D Polyline, 2D Keypoint, Classification, Segmentation |
| Videos | MP4 | Classification |
| Audios | MP3, wav | Classification |
| Texts | txt | Classification |

 - **Image data viewer** 

   Image data visualization support viewing image data in tables(default) or a larger view.

   - Table view

     ![image](https://user-images.githubusercontent.com/101299635/234798959-ba214fb9-bf94-413b-b6b5-81d0d9f5ba40.png)

   - Full view

     The left side of the full view displays the annotation information supporting choosing whether the annotations are to display or not.

     ![image](https://user-images.githubusercontent.com/101299635/234799661-9b33cf77-975f-40be-8f87-55a705848660.png)

 - **Video data viewer**
  
   - Table view

     ![img_v2_9a04dddd-6c39-4b45-8763-075798f1e48g](https://user-images.githubusercontent.com/101299635/234829713-42ca7580-d2b6-4e98-b9d4-92f8c0e2585d.jpg)

   - Full view

     Video data visualization support viewing audio data in tables(default) or a larger view.

     1 The left side of the full view displays the annotation information.

     2 The lower right corner can play/pause, play volume, play speed and play in full screen.

     ![img_v2_3e961d3c-4d57-42ff-b18a-1d9cf65db64g](https://user-images.githubusercontent.com/101299635/234829798-ea4f6cb2-3c88-43dd-88d9-55e43db3a95c.jpg)

 - **Audio data viewer**
 
   Audio data visualization support viewing audio data in tables(default) or a larger view.

   - Table view

     ![image](https://user-images.githubusercontent.com/101299635/234803932-59089931-00c3-4d12-b101-5d4f11191df3.png)

   - Full view

     1 The left side of the full view displays the annotation information

     2 The lower right corner can play/pause and play volume. Click More, can download and adjust the play speed.

     ![image](https://user-images.githubusercontent.com/101299635/234804126-3f6f76b0-95ba-4bf4-9150-6ee7c9563c54.png)

 - **Text data visualization**

   Text data visualization support viewing audio data in tables(default) or a larger view

   ![image](https://user-images.githubusercontent.com/101299635/234797034-84f2c866-a06a-4552-a292-966d9e8522d5.png)

### Switch to a specific version

Click the droplist to switch to the specific version

![image](https://user-images.githubusercontent.com/101299635/234826002-98678c2e-469b-402c-a3ed-0fa7af6157f5.png)

#### Search and filter

An interactive and friendly search of the data.

![image](https://user-images.githubusercontent.com/101299635/234832373-c5dd7d53-584d-4235-9021-519ae1c25123.png)

## Share a dataset

Easily switch between shared and unshared modes for dataset versions by clicking the designated button. If the dataset is part of a public project, a shared dataset can be accessed and utilized by all users. For more information, see [Roles and permissions](https://doc.starwhale.ai/docs/concepts/roles-permissions)

![image](https://user-images.githubusercontent.com/101299635/234837932-18a7270f-a1c5-48f6-8aec-5f58458d357c.png)

## Remove a dataset and restore a dataset

### To remove

Click **Remove** to remove the dataset to trash.

:::important
If you confirm to remove, all versions of a dataset will be removed.
:::

![image](https://user-images.githubusercontent.com/101299635/234845251-f96b7cb5-c2e5-4257-be18-ed89840eb323.png)

### To restore

You can go to **Trash** to restore your datasets. For more information, see [How to restore trash](https://github.com/lijing-susan/starwhale/blob/lijing-docs/docs/docs/instances/server/Trash/Trash.md#to-restore)

