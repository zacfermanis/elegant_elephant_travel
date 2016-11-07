'use strict';

describe('Controller Tests', function() {

    describe('Vacation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVacation, MockCard, MockQuote, MockPassenger, MockCustomer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVacation = jasmine.createSpy('MockVacation');
            MockCard = jasmine.createSpy('MockCard');
            MockQuote = jasmine.createSpy('MockQuote');
            MockPassenger = jasmine.createSpy('MockPassenger');
            MockCustomer = jasmine.createSpy('MockCustomer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Vacation': MockVacation,
                'Card': MockCard,
                'Quote': MockQuote,
                'Passenger': MockPassenger,
                'Customer': MockCustomer
            };
            createController = function() {
                $injector.get('$controller')("VacationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'elegantelephantApp:vacationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
