package net.bucssa.buassist.Util;

/**
 * Created by shinji on 2018/4/4.
 */

public class CreditUtils {

    /* 学霸值等级列表： */
    /* LV1: 0 - 20 */
    /* LV2: 20 - 50 */
    /* LV3: 50 - 100 */
    /* LV4: 100 - 200*/
    /* LV5: 200 - */


    /**
     * 根据小组的学霸值数量，生成当前等级信息
     * @param credit
     * @return
     */
    public static Level calculateLevel(int credit) {
        Level level = new Level();
        /* 先将总数计入level object */
        level.totalCredit = credit;

        if (credit < 20) {  /* 如果credit小于20, 则等级为1，需要到达20*/
            level.currentLV = 1;
            level.creditForNextLV = 20;
        } else if (credit < 50 && credit >= 20) {   /* 如果credit小于50并大于等于20, 则等级为2 */
            level.currentLV = 2;
            level.creditForNextLV = 50;
        }  else if (credit - 100 < 0 && credit - 50 >= 0) {     /* 如果credit小于100并大于等于50, 则等级为3 */
            level.currentLV = 3;
            level.creditForNextLV = 100;
        } else if (credit - 200 < 0 && credit - 100 >= 0) {     /* 如果credit小于200并大于等于100, 则等级为4 */
            level.currentLV = 4;
            level.creditForNextLV = 200;
        } else {        /* 如果credit大于等于200, 则等级为5 */
            level.currentLV = 5;
            level.creditForNextLV = 0;  /* 等级达到上限，所以不存在下一级所需等级 */
        }

        return level;
    }


    public static class Level {
        private int currentLV;
        private int creditForNextLV;
        private int totalCredit;

        public int getCreditForNextLV() {
            return creditForNextLV;
        }

        public int getCurrentLV() {
            return currentLV;
        }

        public int getTotalCredit() {
            return totalCredit;
        }
    }

}
