angular.module("DynamicForms")

.directive('dynamicComboBox', function () {
    return {
        restrict: "E",
        replace: true,
        templateUrl: "js/dynamicForms/dynamic-combo-box.html",
        controller: "dynamicComboBoxController"
    };
}).
controller("dynamicComboBoxController", ['$scope', '$http', function ($scope, $http) {

        var host = "http://localhost:8080/UltraForms/list";

        this.getListByName = function (name)
        {
            return $http({
                url: host + "?name=" + name,
                method: "GET"
            }).success(function (data) {
                return data;
            });
        };

        $scope.listItem = getListByName($scope.fieldData.valueList);

}]);