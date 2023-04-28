## Resource purchasing

**Overview**

You can purchase on-demand resources within Starwhale. With On-Demand Resources, you can pay for computational capacity based on the minutes that your jobs are running, with no long-term commitments.

When submitting a job, you'll need to select the appropriate resource configuration. On-Demand Resources are billed every five minutes in accordance with the duration of your job. 

:::tip
The job duration is calculated from the start of the job to its finish.
:::

**On-Demand Resource limits**

Before running a job, ensure that your Starwhale account has sufficient balance. Specifically, there should be no less than the billing fees for a five-minute period. 

:::important
If your account balance becomes overdue during job execution, the system will automatically terminate the job until the balance is recharged. After recharging,  the job needs to restar.
:::
