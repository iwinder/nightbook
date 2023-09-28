var PageInfo = (function () {
    function PageInfo(data) {
        Object.call(this, data);
        this.first = data ? data.first : true;
        this.last =  data ? data.last : false;
        this.number =  data ? data.number : 0;
        this.numberOfElements = data ? data.numberOfElements : 0;
        this.size =  data ?  data.size : 0;
        this.sort =   data ? data.sort : null;
        this.totalElements =  data ? data.totalElements : 0;
        this.totalPages =  data ? data.totalPages : 0;
        this.content =  data ? data.content : [];
    }
    PageInfo.prototype = new Object();
    PageInfo.prototype.constructor = PageInfo;
    return PageInfo;
})();
module.exports = {
    PageInfo: PageInfo,
};
