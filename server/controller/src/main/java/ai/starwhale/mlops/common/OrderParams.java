/*
 * Copyright 2022 Starwhale, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.starwhale.mlops.common;

import cn.hutool.db.sql.Direction;
import cn.hutool.db.sql.Order;
import java.util.Map;
import javax.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class OrderParams extends BaseParams {

    private String sort;

    private int order;

    public String getOrderSQL(Map<String, String> fieldMap) throws ValidationException {
        if(StringUtils.hasText(sort)) {
            if(fieldMap == null || !fieldMap.containsKey(sort)) {
                throw new ValidationException();
            }
            return new Order(fieldMap.get(sort), order < 0 ? Direction.DESC : Direction.ASC).toString();
        } else {
            return "";
        }
    }
}