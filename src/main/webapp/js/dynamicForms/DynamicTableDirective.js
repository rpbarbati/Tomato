angular.module("DynamicForms")

.directive('dynamicTable', function() {
	return {
		restrict: "E",
		scope : {
			name: '@',
//			view: '@',
			path: '@',
			disabled: '&',
			select: '&'
		},
		controller: "DynamicTableController",
		templateUrl: 'http://localhost:8084/udb/js/dynamicForms/DynamicTableTemplate.html'
	
	};
})

.controller("DynamicTableController", ['$scope', 'SharedDataFactory', 'FormSchemaServices', '$rootScope', "EntityServices", function($scope, SharedDataService, formSchemaServices, $rootScope, entityServices) {
	
	$scope.formSchema = {};
	
	// This will become a reference to the __EntityCollection.__EntityInstances being held in sharedDataService
	$scope.entityInstances = [];
	
	$scope.evaluateExpression = function(entity, name)
	{
		var expression = $scope.formSchema.Expressions[name];
			
		var realExp = expression.replace(/SV:/g, "\$scope.currentEntity.");
			
		$scope.currentEntity = entity;
		
		var result = eval(realExp);

		return result;
	};
	
	$scope.$on("DE_EntityDataChanged", function(event, data) {
		
		$scope.entityInstances = SharedDataService.getMountPoint($scope.path);
		
		$scope.loadSchema();
	});

	$scope.loadSchema = function()
	{
		// Load form schema
		formSchemaServices.loadFormSchema($scope.name).
			success(function(data) {
				$scope.formSchema = data;
		});
	};
	
	$scope.deleteSelectedRows = function()
	{
		for (var i = 0; i < $scope.entityInstances.length; i++)
		{
			var entity = $scope.entityInstances[i];
			
			if (entity.__Selected)
			{
				entity.__Delete = true;
				entity.__Dirty = true;
			}
		}
		
		// Commit changes
		$scope.commitChanges();
	};
	
	$scope.addTableRow = function()
	{
	   	entityServices.newEntity($scope.name, true, false, null)
	   		.success(function (data) {
	   			$scope.entityInstances.push(data[$scope.name].__Entity);
 		});
	};
	
	$scope.commitChanges = function()
	{
		$rootScope.$broadcast("DT_SaveData", true);
	};
	
	$scope.selectRow = function()
	{
		// Broadcast the first selected row we encounter
		for (var i = 0; i < $scope.entityInstances.length; i++)
		{
			var entity = $scope.entityInstances[i];
			
			if (entity.__Selected)
				$rootScope.$broadcast("DT_SelectRow", entity);
		}
	};

}]);