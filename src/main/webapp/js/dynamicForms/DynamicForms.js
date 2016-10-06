angular.module('DynamicForms', [])



//.service('SharedMethods', ['$http', '$sce', function($http, $sce) {
//	
//	this.loadHTML = function(url) {
//		 return $http({
//		        url: url,
//		        method: "GET"
//		    }).success(function (data) {
//		    	return $sce.trustAsHtml(data);
//		    });
//	};	
//	
//}])


.directive('dynamic', function($compile) {
	return {
		restrict: "A",
	    replace: true,		
		link: function(scope, element, attrs) {
			 scope.$watch(attrs.dynamic, function(html) {
			        element.html(html);
			        $compile(element.contents())(scope);
			});
		}
	};
});
