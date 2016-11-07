'use strict';

describe('Controller Tests', function() {

    describe('Quote Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockQuote, MockPassenger, MockVacation, MockQuoteRequest;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockQuote = jasmine.createSpy('MockQuote');
            MockPassenger = jasmine.createSpy('MockPassenger');
            MockVacation = jasmine.createSpy('MockVacation');
            MockQuoteRequest = jasmine.createSpy('MockQuoteRequest');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Quote': MockQuote,
                'Passenger': MockPassenger,
                'Vacation': MockVacation,
                'QuoteRequest': MockQuoteRequest
            };
            createController = function() {
                $injector.get('$controller')("QuoteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'elegantelephantApp:quoteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
