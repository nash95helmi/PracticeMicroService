package com.practice.module.base;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Pagination {
    private String currentPage;
    private String pageSize;
    private String totalPage;
    private String totalRecord;

    public static final class Builder {
        private String currentPage;
        private String pageSize;
        private String totalPage;
        private String totalRecord;

        public Builder() { }

        public static Builder instance() {
            return new Builder();
        }

        public Builder setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public Builder setPageSize(String pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder setTotalPage(String totalPage) {
            this.totalPage = totalPage;
            return this;
        }

        public Builder setTotalRecord(String totalRecord) {
            this.totalRecord = totalRecord;
            return this;
        }

        public Pagination build() {
            Pagination pagination = new Pagination();
            pagination.setCurrentPage(currentPage);
            pagination.setPageSize(pageSize);
            pagination.setTotalPage(totalPage);
            pagination.setTotalRecord(totalRecord);
            return pagination;
        }
    }
}
