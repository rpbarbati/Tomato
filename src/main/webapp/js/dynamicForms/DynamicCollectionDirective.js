angular.module("DynamicForms")

.directive('dynamicCollection', function() {
	return {
		restrict: "E",
		scope : {
			name: '@',
			deep: '@',
			filter: '@'
		},
		controller: "DynamicCollectionController"
}}).

controller("DynamicCollectionController", ['$scope', 'SharedDataFactory', '$rootScope', function($scope, sharedDataService, $rootScope) {
	
	$scope.entityCollection = {};
	
	$scope.$on("DE_EntityDataChanged", function(event, data) {
		
		$scope.entityCollection = sharedDataService.getData();
	});
	
	$scope.$on("DT_SaveData", function(event, data) {
		sharedDataService.save();
	});
	
	$scope.load = function(name, deep, filter)
	{
		$scope.name = name;
		$scope.deep = (deep == "true");
		$scope.filter = filter;
		
		sharedDataService.loadEntityCollection($scope.name, $scope.deep, $scope.filter);	
	};
	
	// Load the data
	$scope.load($scope.name, ($scope.deep == 'true'), $scope.filter);
	
}]);