angular.module("DynamicForms").
	controller("entityTesterController", ['$scope', 'SharedDataFactory', function($scope, sharedDataService) {
		
		$scope.name = "";
		
		$scope.deep = false;
		$scope.collection = false;
		$scope.filter = "";
		
		
		$scope.data = {};
		
		// Begin with an empty application
		$scope.data.entity = {};
		$scope.data.textEntity = "";
		
		$scope.$on("DE_EntityDataChanged", function(event, data) {
			
			$scope.data.entity = data;
			$scope.data.textEntity = angular.toJson(data, true);
		});
		

//		// Watch for entity template load
//		$scope.$watch(sharedDataService.getEntityTemplate, function(newValue, oldValue, scope) {
////			$scope.data.entity = newValue;
//			$scope.data.textEntity = angular.toJson(newValue, true);
//		});
//		
//		
//		
//		// Watch for entity load
//		$scope.$watch(sharedDataService.getData, function(newValue, oldValue, scope) {
//			$scope.data.entity = newValue;
//			$scope.data.textEntity = angular.toJson(newValue, true);
//		});
		
		// Load the entity template

		$scope.newEntity = function()
		{
//			if ($scope.collection)
//			{
//				sharedDataService.loadEntityCollection($scope.name, $scope.deep, $scope.filter);
//			}
//			else
//			{
//				var json = angular.fromJson($scope.filter);
//				
//				sharedDataService.loadEntity($scope.name, $scope.deep, json);
//			}
			
			sharedDataService.newEntity($scope.name, $scope.deep, $scope.collection, $scope.filter);
			
			// Should execute watch 
		};
		
//			$scope.data.textEntity = angular.toJson(dataServices.getEntityTemplate(), true);
//			success(function(data) {
//				$scope.data.textEntity = angular.toJson(data, true);
//			});
			
//			entityServices.newEntity($scope.schema, $scope.name, $scope.deep, $scope.collection, $scope.filter).
//				success(function(data) {
//					$scope.data.textEntity = angular.toJson(data, true);
//				});
//		};

	
		$scope.loadEntity = function()
		{

//			if ($scope.filter != null & $scope.filter != "" & $scope.filter != undefined)
//			{
//				// Load using the filter string for parameters
//				if ($scope.collection)
//				{
//					sharedDataService.loadEntityCollection($scope.name, $scope.deep, $scope.filter);
//				}
//				else
//				{
//					var json = angular.fromJson($scope.filter);
//					
//					sharedDataService.loadEntity($scope.name, $scope.deep, json);
//				}
//			}
//			else
//			{
				// Load using the structure in the textEntity window
				var json = angular.fromJson($scope.data.textEntity, true);
				
				sharedDataService.entityTemplate = json;
				
				sharedDataService.load();
//			}
			
//			sharedDataService.loadTemplate(json);

			// Should execute watch
		};
		
		$scope.saveEntity = function()
		{
			var json = angular.fromJson($scope.data.textEntity);

			// Do not call setData or else we get a broadcast
			sharedDataService.data = json;
			
			sharedDataService.save();
		};
		
		$scope.clear = function()
		{
			$scope.data.textEntity = null;
		};

	}		
]);

