(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('quote-request', {
            parent: 'entity',
            url: '/quote-request',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'QuoteRequests'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/quote-request/quote-requests.html',
                    controller: 'QuoteRequestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('quote-request-detail', {
            parent: 'entity',
            url: '/quote-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'QuoteRequest'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/quote-request/quote-request-detail.html',
                    controller: 'QuoteRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'QuoteRequest', function($stateParams, QuoteRequest) {
                    return QuoteRequest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'quote-request',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('quote-request-detail.edit', {
            parent: 'quote-request-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote-request/quote-request-dialog.html',
                    controller: 'QuoteRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['QuoteRequest', function(QuoteRequest) {
                            return QuoteRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('quote-request.new', {
            parent: 'quote-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote-request/quote-request-dialog.html',
                    controller: 'QuoteRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                maximumBudget: null,
                                destination: null,
                                destinationAirport: null,
                                departureDate: null,
                                returnDate: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('quote-request', null, { reload: 'quote-request' });
                }, function() {
                    $state.go('quote-request');
                });
            }]
        })
        .state('quote-request.edit', {
            parent: 'quote-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote-request/quote-request-dialog.html',
                    controller: 'QuoteRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['QuoteRequest', function(QuoteRequest) {
                            return QuoteRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('quote-request', null, { reload: 'quote-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('quote-request.delete', {
            parent: 'quote-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote-request/quote-request-delete-dialog.html',
                    controller: 'QuoteRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['QuoteRequest', function(QuoteRequest) {
                            return QuoteRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('quote-request', null, { reload: 'quote-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
