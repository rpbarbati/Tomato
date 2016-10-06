angular.module("DynamicForms")

.service('FormSchemaServices', ['$http', function ($http) {

    var host = "http://localhost:8084/udb/forms";

    this.loadFormSchema = function (name)
    {
        return $http({
            url: host + "?name=" + name + "&view=" + name,
            method: "GET"
        }).success(function (data) {
            return data;
        });
    };

}]);