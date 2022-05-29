package com.softvision.books.services.domain;

public class PageBean {

    private Integer page;

    private Integer maxPage;

    private Integer size;

    private Long total;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(Integer maxPage) {
        this.maxPage = maxPage;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public static PageBean empty() {
        return new Builder()
                .page(0)
                .maxPage(0)
                .size(0)
                .total(0L)
                .build();
    }

    public static class Builder {

        private Integer page;

        private Integer maxPage;

        private Integer size;

        private Long total;

        public Builder page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder maxPage(Integer maxPage) {
            this.maxPage = maxPage;
            return this;
        }

        public Builder size(Integer size) {
            this.size = size;
            return this;
        }

        public Builder total(Long total) {
            this.total = total;
            return this;
        }

        public PageBean build() {

            final PageBean pageBean = new PageBean();

            pageBean.setMaxPage(this.maxPage);
            pageBean.setPage(this.page);
            pageBean.setSize(this.size);
            pageBean.setTotal(this.total);

            return pageBean;
        }
    }
}
