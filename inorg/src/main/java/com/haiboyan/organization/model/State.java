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

public enum State {
    IN_POSITION("In postion"),
    ON_LEAVE("On Leave"),
    QUITTED("Quitted");

    private final String description;

    State(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean valid() {
        return this == IN_POSITION || this == ON_LEAVE;
    }

    public boolean validNewState(State newState) {
        switch (newState) {
            case IN_POSITION:
                return ON_LEAVE == this;
            case ON_LEAVE:
                return IN_POSITION == this;
            case QUITTED:
                return IN_POSITION == this;
            default:
                return false;
        }
    }
}
