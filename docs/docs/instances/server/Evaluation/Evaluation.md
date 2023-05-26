---
title:Evaluation in Starwhale
---

## Evaluation table

In the evaluation table, you can see all the evaluations of one project in one place.

Each time you run an evaluation with Starwhale Web UI, Starwhale Client or Python SDK, it's added to the top of the table.

![image](https://github.com/lijing-susan/starwhale/assets/101299635/0fececdd-3675-4dcf-a2d2-36e52393a5eb)

### Custom views of the evaluation table

You can save your evaluation table configuration to a custom view, and it's visible to anymember of the project.

**Save and save as a new view**

If you edit some configurations including search, filter and table columns, the **Save as** and **Save** button will automatically be displayed. 

 - Click **Save** to save the configurations to the current view.

  ![image](https://user-images.githubusercontent.com/101299635/236804900-d67bc11e-df68-4d13-b7e9-cf35f8ca094e.png)

 - CLick **Save as**, type a name for the view, then click **Save** to save to a new view.
 
 ![image](https://user-images.githubusercontent.com/101299635/236805810-358e5d68-2009-40bc-a16b-c5b4e12427dd.png)

 ![image](https://user-images.githubusercontent.com/101299635/236806207-1d8f4a30-321a-496a-94fd-4a2cf2d8a4e1.png)

**Add a new view**

1 Click **Add view** in the custom view drop list

![image](https://user-images.githubusercontent.com/101299635/236823774-620a8ec9-0307-436d-a2bd-9dceb0703788.png)

2 Type a name for the view, then edit the configurations, such as filters, columns and sorts, then click **Save** to save the view

 ![image](https://user-images.githubusercontent.com/101299635/236806207-1d8f4a30-321a-496a-94fd-4a2cf2d8a4e1.png)

**Manage views**

1 Click **Manage views** in the custom view drop list

![image](https://user-images.githubusercontent.com/101299635/236825459-3edbd540-456e-4e50-86a3-74f6ce038c1f.png)

2 To change the default view or the visibility of views, click **Save** to save the changes

![image](https://user-images.githubusercontent.com/101299635/236826075-79de0048-30b1-4670-9594-66b7645796f7.png)

### Selecting evaluations to compare

To select evaluations to compare by clicking the checkbox to include or exclude evaluations.

![image](https://github.com/lijing-susan/starwhale/assets/101299635/3ca880ae-7f57-4e54-bd1c-24952f756cd6)

![image](https://github.com/lijing-susan/starwhale/assets/101299635/1a0c81c2-fd2e-411f-9ef2-26b2d37fd77d)

### Search and filter

## Evaluation details

### Evaluation summary

The summary information of an evaluation, including evaluation metrics, evalution status, model and its version name, runtime and its version name, evaluation started and finished time, owner, resource pool, and step specification.

![image](https://github.com/lijing-susan/starwhale/assets/101299635/177621b6-85f1-4eed-b54b-6f4d92713efe)

### Custom Panels

**To create**

To create a Panel, clicking **Add Panel** button which is at the bottom of the page

A Panel can be created only if you have "Project owner" permission.

![image](https://github.com/lijing-susan/starwhale/assets/101299635/a9b85788-8d87-44e9-a9f7-3603c0fe4e13)

Or to create a Panel, clicking **More** button, then click **Add Panel Above** or **Add Panel Below** 

![image](https://github.com/lijing-susan/starwhale/assets/101299635/25fdf551-4166-4b73-8240-8aed482d16af)

**To rename**

To rename a Panel, clicking **More** button, then click **Rename** and input a new name.

![image](https://github.com/lijing-susan/starwhale/assets/101299635/e16d7d20-2f87-4e83-a507-1724855735de)

**To delete**

To delete a Panel, clicking **More** button, then click **Delete**.

![image](https://github.com/lijing-susan/starwhale/assets/101299635/e6d46ac7-d5db-40e0-8573-1a532bfecc49)

**To change the display order of the panel**

First to hover on the top of the panel，then click the button in the middle, and drag the panel to your expecting position to change its display oder.

![image](https://github.com/lijing-susan/starwhale/assets/101299635/ba97aa85-b963-4edd-9b2c-888ff63878d4)

### Custom Charts

**To create**

To create a Chart, clicking **Add Chart** button which is on the right side of the panel.

![image](https://github.com/lijing-susan/starwhale/assets/101299635/489a679e-ea01-4cbf-ad9f-e8aed59231a3)

Types of charts: Table, Roc-Auc chart, and Confusion Matrix.

**To edit**

Hover on the top of the chart，click the **Configuration** button, and then select **Edit Chart**.

![image](https://github.com/lijing-susan/starwhale/assets/101299635/7e1291a3-38a7-4d6a-8869-b63037ec4bd3)

**To delete**

Hover on the top of the chart，click the **Configuration** button, and then select **Delete Chart**.

![image](https://github.com/lijing-susan/starwhale/assets/101299635/b15d4113-2774-42ee-b82f-c07cf0910845)

### Evaluation tasks and logs

**Tasks**

Click **Tasks** tab to see all the tasks of an evaluation, or click a specific bar in the DAG to go to the task table.

![image](https://github.com/lijing-susan/starwhale/assets/101299635/2f580fa4-868e-4d8d-b14a-ea72396fd757)

**Logs**

Click **View logs** buttton at the end of the task table to view logs.

![image](https://github.com/lijing-susan/starwhale/assets/101299635/f1b896a7-f55b-40c9-a419-f44e0a3575fa)
