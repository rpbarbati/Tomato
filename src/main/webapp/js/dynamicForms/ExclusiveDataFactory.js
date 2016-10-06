//
//function ExclusiveDataFactory($rootScope, entityServices) {
//
//	this.entityTemplate = {};
//	
//	this.data = {};
//	
//
//	this.setData = function(data)
//	{
//		this.data = data;
//		
//		$rootScope.$broadcast("DE_EntityDataChanged", this.data);
//	};
//	
//	this.getData = function()
//	{
//		return this.data;
//	};
//	
//  
//    
//    this.loadEntity = function(name, deep, values)
//    {
//    	this.name = name;
//    	
//    	entityServices.newEntity(name, deep, false, null)
// 		.success(function (data) {
// 			this.entityTemplate = data;
// 			
// 			// Apply values then load
// 			var keys = Object.keys(values);
//
// 			// Add values to template
// 			for (var i = 0; i < keys.length; i++)
// 			{
// 				this.entityTemplate[this.name].__Entity[keys[i]] = values[keys[i]];
// 			}
//
// 			this.load();
//		});
//    };
//    
//     
//	this.load = function()
//    {
//    	entityServices.loadEntity(this.entityTemplate).
//    		success(function(data) {
//    			this.setData(data);
//    		}
//    	);
//    };
//    
//	this.save = function()
//    {
//    	entityServices.saveEntity(this.data).
//    		success(function(data) {
//    			this.setData(data);
//    	});
//    };
//    
//    this.loadEntityCollection = function(name, deep, filter)
//    {
//      	this.name = name;
//        
//    	entityServices.newEntity(name, deep, true, filter)
// 		.success(function (data) {
// 			this.entityTemplate = data;
// 
// 			this.load();
//		});
//    };
//    
//    
//
//    
////    this.expression = null;
////    
////	// Evaluate expressions (parameter is name of expression (in current form
////	// schema))
////    // Variable references (SV:) are set to the data root for entities
////	// (this.entity.something goes here)
////    // Variable references (SV:) are set to the current entity in collection for
////	// collections
////	this.evaluateExpression = function(name)
////	{
////		var expression = this.formSchema.Expressions[name];
////		var realExp = expression.replace(/SV:/g, "\this.entity.");
////		var result = eval(realExp);
////
////		return result;
////	};
//	
//	// Walk the data from the top using the passed path (and internal entity structure knowledge)
//	// Path is a dotted set of Entity names (for example, "location.vehicle" or "application.exstream.exstream_switch")
//	// Returns the top of entity data (__Entity__) for an Entity
//	// Returns the __EntityInstances__ array for EntityCollections
//   	this.getMountPoint = function(path)
//   	{
//   		var names = path.split(".");
//
//   		var data  = this.data;
//
//   		for (var index = 0; index < names.length; index++)
//   		{
//   			var path = names[index] + "." + names[++index];
//   			
//   			if (data[path] == undefined)
//   				return this.entityTemplate;
//
//   			else
//   			{		
//	   			if (data[path].__Entity != undefined)
//	   				data = data[path].__Entity;
//
//	   			else if (data[path].__EntityCollection != undefined)
//	   				data = data[path].__EntityCollection.__EntityInstances;
//   			}
//   		}
//   		
//   		return data;
//   	};
//  
//}