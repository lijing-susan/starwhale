/*
 * Copyright 2022.1-2022
 * StarWhale.ai All right reserved. This software is the confidential and proprietary information of
 * StarWhale.ai ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with StarWhale.ai.
 */

package ai.starwhale.mlops.resulting.impl.clsmulti.metrics;

import ai.starwhale.mlops.resulting.Indicator;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.NoArgsConstructor;

/**
 * indicator for Multiclass classification
 */
@NoArgsConstructor
public class MCIndicator extends Indicator<AtomicInteger> {

    String label;

    String prediction;

    static final String KEY_CONNECTOR="-";

    public MCIndicator(String label,String prediction){
        this.label = label;
        this.prediction = prediction;
        this.name = label + KEY_CONNECTOR + prediction;
        this.value = new AtomicInteger(1);
    }

    public MCIndicator(String key,Integer value){
        final String[] split = key.split(KEY_CONNECTOR);
        this.name = key;
        this.label = split[0];
        this.prediction = split[1];
        this.value = new AtomicInteger(value);
    }

    public boolean right(){
        return label.equals(prediction);
    }

    public String getLabel(){
        return this.label;
    }

    public String getPrediction(){
        return this.prediction;
    }

}