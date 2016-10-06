angular.module('DynamicForms')

.factory("SharedDataFactory", [ '$rootScope', 'EntityServices', function($rootScope, entityServices) {

	var service = {};
	
	service.entityTemplate = {};
	
	service.data = {};
	
	service.setData = function(data)
	{
		service.data = data;
		
		$rootScope.$broadcast("DE_EntityDataChanged", service.data);
	};
	
	service.getData = function()
	{
		return service.data;
	};
	
	service.newEntity = function(name, deep, collection, filter)
    {
    	service.name = name;
    	
    	entityServices.newEntity(name, deep, collection, null)
 		.success(function (data) {
 			service.entityTemplate = data;
 			
 			$rootScope.$broadcast("DE_EntityDataChanged", service.entityTemplate);
 		});
    };
	    
	
    service.loadEntity = function(name, deep, values)
    {
    	service.name = name;
    	
    	entityServices.newEntity(name, deep, false, null)
            .success(function (data) {
 		service.entityTemplate = data;
 			
                    // Apply values then load
                    var keys = Object.keys(values);

                    // Add values to template
                    for (var i = 0; i < keys.length; i++)
                    {
                        var entity = service.entityTemplate[service.name];
                        entity[keys[i]] = values[keys[i]];
                    }

                    service.load();
            });
    };
    
    service.loadEntityCollection = function(name, deep, filter)
    {
      	service.name = name;
        
    	entityServices.newEntity(name, deep, true, filter)
 		.success(function (data) {
 			service.entityTemplate = data;
 
 			service.load();
		});
    };
    
    
    
    service.load = function()
    {
    	entityServices.loadEntity(service.entityTemplate).
    		success(function(data) {
    			service.setData(data);
    		}
    	);
    };
    
    service.save = function()
    {
    	entityServices.saveEntity(service.data).
    		success(function(data) {
    			service.setData(data);
    	});
    };
    

//    service.expression = null;
//    
//	// Evaluate expressions (parameter is name of expression (in current form
//	// schema))
//    // Variable references (SV:) are set to the data root for entities
//	// (service.entity.something goes here)
//    // Variable references (SV:) are set to the current entity in collection for
//	// collections
//	service.evaluateExpression = function(name)
//	{
//		var expression = service.formSchema.Expressions[name];
//		var realExp = expression.replace(/SV:/g, "\service.entity.");
//		var result = eval(realExp);
//
//		return result;
//	};
	
    // Walk the data from the top using the passed path (and internal entity structure knowledge)
    // Path is a dotted set of Entity names (for example, "location.vehicle" or "application.exstream.exstream_switch")
    // Returns the top of entity data (__Entity__) for an Entity
    // Returns the __EntityInstances__ array for EntityCollections
    service.getMountPoint = function(path)
    {
        var names = path.split(".");

        var data  = service.data;

        for (var index = 0; index < names.length; index++)
        {
            // get the schema and name
            var path = names[index] + "." + names[++index];

            if (data[path] == undefined)
                return service.entityTemplate;

            else
            {		
				data = data[path];
				
				// If pointing to a set, return the array element
				if (data.__elements)
					data = data.__elements;
			}
		}

        return data;
    };

    return service;
}]);
