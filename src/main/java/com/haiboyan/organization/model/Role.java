/*
 * Copyright [2017] [Haibo(Tristan) Yan]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haiboyan.organization.model;

public enum Role {
    CEO(1, "CEO"),
    VP(2, "Vice President"),
    Director(3, "Director"),
    Manager(4, "Manager"),
    Permanent_Employee(5, "Permanent Employee"),
    Contractor(5, "Contractor Employee");

    private final int level;

    private final String name;

    Role(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public Role nextLevel() {
        switch (this) {
            case VP:
                return CEO;
            case Director:
                return VP;
            case Manager:
                return Director;
            case Permanent_Employee:
                return Manager;
            default:
                return this;
        }
    }

    public boolean isManager() {
        return level < 5;
    }
}
