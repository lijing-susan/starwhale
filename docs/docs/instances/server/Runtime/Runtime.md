---
title: Runtime in Starwhale
---

## Runtime list

### Runtime display

Starwhale runtime list displays the basic runtime information: Runtime name, the latest runtime version alias, runtime owner, the created time and runtime actions.

![image](https://user-images.githubusercontent.com/101299635/236448328-3d77a384-388b-4135-b0b5-8089ce7af589.png)

### History versions of a runtime

1 Click **Version History** to see all the history versions of a runtime.

![image](https://user-images.githubusercontent.com/101299635/236449156-01fed362-a72b-4218-b5fc-672b907f7200.png)

2 Starwhale runtime history version list displays the basic runtime version information: Version ID, version alias, whether the version is shared, created time of the version, owner, and runtime version actions.

![image](https://user-images.githubusercontent.com/101299635/234825710-37a13e18-7df1-471c-b339-445f49435c91.png)

:::tip
If a runtime version is shared, it is visible to anyone. If it is not shared, it can only be seen and used by users with permission. 
:::

## Runtime details

Runtime version information consists of three parts: Overview, metadata, and files.

**Overview**: The basic information of a runtime version, including runtime name, runtime version name, runtime version aliases, whether the runtime version is shared, and the created time of a runtime version.

![image](https://user-images.githubusercontent.com/101299635/236450424-4d27a47d-761e-4cff-a24f-ff8b4964aefc.png)

:::tip
Switch the button to set a runtime version to a shared mode if the runtime belongs to a public project. A shared runtime version can be seen and used for everyone. For more information, see [Roles and permissions](https://doc.starwhale.ai/docs/concepts/roles-permissions) 
:::

**Metadata**: The metadata of the runtime, including artifacts information, building os information, configuration information, dependencies information, and environment information.

![image](https://user-images.githubusercontent.com/101299635/236451479-ba4e330b-650f-41a5-9cd8-66f1ba0a845a.png)

**Files**: Displaying the `runtime.swrt` and its size

![image](https://user-images.githubusercontent.com/101299635/236451573-4d1b3289-e6c8-4099-9650-bf7c0bf0bf12.png)

### Switch to a specific version

Click the droplist to switch to the specific version

![image](https://user-images.githubusercontent.com/101299635/236452461-c622e1f1-cffd-4d42-92e7-dfb58b1f2f41.png)

## Share a runtime version

Easily share your runtime version by activating the 'shared' button, while deactivating it will stop sharing. If the runtime is part of a public project, a shared runtime version can be accessed and utilized by all users. For more information, see [Roles and permissions](https://doc.starwhale.ai/docs/concepts/roles-permissions)

![image](https://user-images.githubusercontent.com/101299635/236457794-29b81612-7ea4-407f-a6cc-1b35028d9af7.png)

## Remove or restore a runtime

### To remove

Click **Remove** to remove the runtime to trash.

:::important
If you confirm to remove, all versions of a runtime will be removed.
:::

![image](https://user-images.githubusercontent.com/101299635/236459483-254aa2e5-9b2e-4de5-ad45-57ca3b2103ff.png)

### To restore

You can go to **Trash** to restore your runtimes. For more information, see [How to restore trash](https://github.com/lijing-susan/starwhale/blob/lijing-docs/docs/docs/instances/server/Trash/Trash.md#to-restore)
