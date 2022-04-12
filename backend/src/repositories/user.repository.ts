import {inject, Getter} from '@loopback/core';
import {DefaultCrudRepository, repository, HasOneRepositoryFactory, HasManyRepositoryFactory} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {User, UserRelations, UserLocation, Ticket, Event, TicketRequest} from '../models';
import {UserLocationRepository} from './user-location.repository';
import {TicketRepository} from './ticket.repository';
import {EventRepository} from './event.repository';
import {TicketRequestRepository} from './ticket-request.repository';

export class UserRepository extends DefaultCrudRepository<
  User,
  typeof User.prototype.id,
  UserRelations
> {

  public readonly userLocation: HasOneRepositoryFactory<UserLocation, typeof User.prototype.id>;

  public readonly tickets: HasManyRepositoryFactory<Ticket, typeof User.prototype.id>;

  public readonly events: HasManyRepositoryFactory<Event, typeof User.prototype.id>;

  public readonly ticketRequests: HasManyRepositoryFactory<TicketRequest, typeof User.prototype.id>;

  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource, @repository.getter('UserLocationRepository') protected userLocationRepositoryGetter: Getter<UserLocationRepository>, @repository.getter('TicketRepository') protected ticketRepositoryGetter: Getter<TicketRepository>, @repository.getter('EventRepository') protected eventRepositoryGetter: Getter<EventRepository>, @repository.getter('TicketRequestRepository') protected ticketRequestRepositoryGetter: Getter<TicketRequestRepository>,
  ) {
    super(User, dataSource);
    this.ticketRequests = this.createHasManyRepositoryFactoryFor('ticketRequests', ticketRequestRepositoryGetter,);
    this.events = this.createHasManyRepositoryFactoryFor('events', eventRepositoryGetter,);
    this.registerInclusionResolver('events', this.events.inclusionResolver);
    this.tickets = this.createHasManyRepositoryFactoryFor('tickets', ticketRepositoryGetter,);
    this.registerInclusionResolver('tickets', this.tickets.inclusionResolver);
    this.userLocation = this.createHasOneRepositoryFactoryFor('userLocation', userLocationRepositoryGetter);
    this.registerInclusionResolver('userLocation', this.userLocation.inclusionResolver);
  }
}
