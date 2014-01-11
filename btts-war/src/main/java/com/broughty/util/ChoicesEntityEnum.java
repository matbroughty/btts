package com.broughty.util;

import java.util.Date;

/**
 * Fields on the Choices entity
 *
 * Created by matbroughty on 04/01/14.
 */
public enum ChoicesEntityEnum {
    PLAYER("player", String.class),
    DATE("date", Date.class),
    CHOICE_ONE("choice1", String.class),
    CHOICE_TWO("choice2", String.class),
    CHOICE_THREE("choice3", String.class),
    CHOICE_FOUR("choice4", String.class),
    CHOICE_ONE_RESULT("choice1Result", Boolean.class),
    CHOICE_TWO_RESULT("choice2Result", Boolean.class),
    CHOICE_THREE_RESULT("choice3Result", Boolean.class),
    CHOICE_FOUR_RESULT("choice4Result", Boolean.class),
    CHOICE_ONE_POINTS("choice1Points", int.class),
    CHOICE_TWO_POINTS("choice2Points", int.class),
    CHOICE_THREE_POINTS("choice3Points", int.class),
    CHOICE_FOUR_POINTS("choice4Points", int.class),
    ALERTED("alerted", Boolean.class),
    DEFAULT("defaultChoices", Boolean.class);

    /**
     * Name of property on entity
     */
    String fieldName;

    /**
     * Class of saved property
     */
    Class clazz;

    ChoicesEntityEnum(String fieldName, Class clazz) {
        this.fieldName = fieldName;
        this.clazz = clazz;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
