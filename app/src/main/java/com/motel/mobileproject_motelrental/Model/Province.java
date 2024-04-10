package com.motel.mobileproject_motelrental.Model;

import java.util.List;

public class Province {
        private String id;
        private String name;
        private String type;
        private String displayName;
        private List<District> districts;

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public String getDisplayName() {
                return displayName;
        }

        public void setDisplayName(String displayName) {
                this.displayName = displayName;
        }

        public List<District> getDistricts() {
                return districts;
        }

        public void setDistricts(List<District> districts) {
                this.districts = districts;
        }
}
