angular.module("DynamicForms")

.service('EntityServices', ['$http', function ($http) {

    var host = "http://localhost:8084/udb/entities";

    this.newEntity = function (name, deep, collection, filter)
    {
        return $http({
            url: host + "?action=new&name=" + name + "&deep=" + deep + "&collection=" + collection + "&filter=" + filter,
            method: "GET"
        }).success(function (data) {
            return data;
        });
    };

    this.loadEntity = function (entity)
    {
        return $http({
            url: host + "?action=load",
            method: 'POST',
            data: entity,
            contentType: "text/plain"
        }).success(function (data) {
            return data;
        });
    };


    this.saveEntity = function (entity)
    {
        return $http({
            url: host + "?action=save",
            method: 'POST',
            data: entity,
            contentType: "texp/plain"
        }).success(function (data) {
            return data;
        });
    };
}]);
