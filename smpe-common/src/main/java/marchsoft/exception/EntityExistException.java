/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package marchsoft.exception;

import cn.hutool.core.util.StrUtil;

/**
 * description: 实体属性已存在异常
 *
 * @author RenShiWei
 * Date: 2020/7/9 22:09
 **/
public class EntityExistException extends RuntimeException {

    public EntityExistException(Class<?> clazz, String field, String val) {
        super(EntityExistException.generateMessage(clazz.getSimpleName(), field, val));
    }

    /**
     * description:生成实体不存在异常信息
     *
     * @param entity 实体名
     * @param field  属性名
     * @param val    参数值
     * @return 异常信息Str
     * @author RenShiWei
     * Date: 2020/11/16 20:42
     */
    private static String generateMessage(String entity, String field, String val) {
        return StrUtil.upperFirst(entity)
                + " with " + field + " " + val + " existed";
    }
}
