(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vacation', {
            parent: 'entity',
            url: '/vacation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Vacations'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vacation/vacations.html',
                    controller: 'VacationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('vacation-detail', {
            parent: 'entity',
            url: '/vacation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Vacation'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vacation/vacation-detail.html',
                    controller: 'VacationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Vacation', function($stateParams, Vacation) {
                    return Vacation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'vacation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('vacation-detail.edit', {
            parent: 'vacation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacation/vacation-dialog.html',
                    controller: 'VacationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vacation', function(Vacation) {
                            return Vacation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vacation.new', {
            parent: 'vacation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacation/vacation-dialog.html',
                    controller: 'VacationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                price: null,
                                description: null,
                                destination: null,
                                destinationAirport: null,
                                departureDate: null,
                                returnDate: null,
                                signature: null,
                                signatureContentType: null,
                                travelProtection: null,
                                status: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vacation', null, { reload: 'vacation' });
                }, function() {
                    $state.go('vacation');
                });
            }]
        })
        .state('vacation.edit', {
            parent: 'vacation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacation/vacation-dialog.html',
                    controller: 'VacationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vacation', function(Vacation) {
                            return Vacation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vacation', null, { reload: 'vacation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vacation.delete', {
            parent: 'vacation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacation/vacation-delete-dialog.html',
                    controller: 'VacationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Vacation', function(Vacation) {
                            return Vacation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vacation', null, { reload: 'vacation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
