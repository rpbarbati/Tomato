angular.module("DynamicForms")

.directive('dynamicForm', function() {
    return {
	restrict: "E",
	scope : {
	    name: '@',
	    path: '@'
	},
	controller: "DynamicFormController",
	templateUrl: 'http://localhost:8084/udb/js/dynamicForms/DynamicFormTemplate.html'
}})

.controller("DynamicFormController", ['$scope', 'SharedDataFactory', 'FormSchemaServices', '$rootScope', function($scope, SharedDataService, FormMetaDataService, $rootScope) {

    $scope.disabled = false;

    $scope.entity = {};

    $scope.$on("DE_EntityDataChanged", function(event, data) {
		$scope.entity = SharedDataService.getMountPoint($scope.path);
    });

    $scope.formSchema = {};

    // Load form schema
    FormMetaDataService.loadFormSchema($scope.name).
	success(function(data) {
		$scope.formSchema = data;
    });

//	$scope.loadHTML = function(fieldData) {
//		fieldData.content = "";
//		sharedMethods.loadHTML(fieldData.html).
//		success(function(data) {
//			fieldData.content = data;
//		});
//	}
}]);