package com.yilinker.expressinternal.model;

/**
 * Created by J.Bautista
 */
public class TabModel {

        private int id;
        private String title;
        private boolean isSelected;
        private int count;

        public TabModel(){

        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public int getCount() {
         return count;
        }

    public void setCount(int count) {
        this.count = count;
    }
}
