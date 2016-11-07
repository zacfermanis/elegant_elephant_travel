'use strict';

describe('Controller Tests', function() {

    describe('QuoteRequest Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockQuoteRequest, MockPassenger, MockQuote, MockCustomer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockQuoteRequest = jasmine.createSpy('MockQuoteRequest');
            MockPassenger = jasmine.createSpy('MockPassenger');
            MockQuote = jasmine.createSpy('MockQuote');
            MockCustomer = jasmine.createSpy('MockCustomer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'QuoteRequest': MockQuoteRequest,
                'Passenger': MockPassenger,
                'Quote': MockQuote,
                'Customer': MockCustomer
            };
            createController = function() {
                $injector.get('$controller')("QuoteRequestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'elegantelephantApp:quoteRequestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
