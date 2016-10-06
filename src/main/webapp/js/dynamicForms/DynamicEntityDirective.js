angular.module("DynamicForms")

.directive('dynamicEntity', function() {
    return {
		restrict: "E",
		scope : {
			name: '@',
			deep: '@',
			keys: '='
		},
		transclude: true,
		template: "<div ng-transclude></div>",
		controller: "DynamicEntityController"
	};
}).

controller("DynamicEntityController", ['$scope', 'SharedDataFactory', '$rootScope', function($scope, sharedDataService, $rootScope) {
	
    $scope.entity = {};

    sharedDataService.loadEntity($scope.name, ($scope.deep === "true"), $scope.keys);

    $scope.$on("DE_EntityDataChanged", function(event, data) 
    {
		$scope.entity = sharedDataService.getData();
    });

    $scope.$on("DT_SaveData", function(event, data) {
        sharedDataService.save();
    });
	
}]);