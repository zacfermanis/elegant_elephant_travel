'use strict';

describe('Controller Tests', function() {

    describe('Passenger Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPassenger, MockQuoteRequest, MockQuote, MockVacation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPassenger = jasmine.createSpy('MockPassenger');
            MockQuoteRequest = jasmine.createSpy('MockQuoteRequest');
            MockQuote = jasmine.createSpy('MockQuote');
            MockVacation = jasmine.createSpy('MockVacation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Passenger': MockPassenger,
                'QuoteRequest': MockQuoteRequest,
                'Quote': MockQuote,
                'Vacation': MockVacation
            };
            createController = function() {
                $injector.get('$controller')("PassengerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'elegantelephantApp:passengerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
