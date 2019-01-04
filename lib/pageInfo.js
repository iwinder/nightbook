// var PageInfo =  (function () {
//     function PageInfo(data) {
//       this.first = data.first;
//       this.last =  data.last;
//       this.number =  data.number;
//       this.numberOfElements =  data.numberOfElements;
//       this.size =  data.size;
//       this.sort =  data.sort;
//       this.totalElements =  data.totalElements;
//       this.totalPages =  data.totalPages;
//       this.content = data.content;
//     }
//     PageInfo.prototype =  new Object();
//     PageInfo.prototype.constructor = PageInfo;
//     return PageInfo;
// })();

var PageInfo = (function () {
    function PageInfo(data) {
        Object.call(this, data);
        console.log("PageInfo data",data);
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