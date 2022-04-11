import {inject, Getter} from '@loopback/core';
import {DefaultCrudRepository, repository, HasManyRepositoryFactory, HasOneRepositoryFactory, BelongsToAccessor} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {Event, EventRelations, Pictures, EventLocation, Ticket, User} from '../models';
import {PicturesRepository} from './pictures.repository';
import {EventLocationRepository} from './event-location.repository';
import {TicketRepository} from './ticket.repository';
import {UserRepository} from './user.repository';

export class EventRepository extends DefaultCrudRepository<
  Event,
  typeof Event.prototype.id,
  EventRelations
> {

  public readonly pictures: HasManyRepositoryFactory<Pictures, typeof Event.prototype.id>;

  public readonly eventLocation: HasOneRepositoryFactory<EventLocation, typeof Event.prototype.id>;

  public readonly tickets: HasManyRepositoryFactory<Ticket, typeof Event.prototype.id>;

  public readonly EventUser: BelongsToAccessor<User, typeof Event.prototype.id>;

  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource, @repository.getter('PicturesRepository') protected picturesRepositoryGetter: Getter<PicturesRepository>, @repository.getter('EventLocationRepository') protected eventLocationRepositoryGetter: Getter<EventLocationRepository>, @repository.getter('TicketRepository') protected ticketRepositoryGetter: Getter<TicketRepository>, @repository.getter('UserRepository') protected userRepositoryGetter: Getter<UserRepository>,
  ) {
    super(Event, dataSource);
    this.EventUser = this.createBelongsToAccessorFor('EventUser', userRepositoryGetter,);
    this.registerInclusionResolver('EventUser', this.EventUser.inclusionResolver);
    this.tickets = this.createHasManyRepositoryFactoryFor('tickets', ticketRepositoryGetter,);
    this.registerInclusionResolver('tickets', this.tickets.inclusionResolver);
    this.eventLocation = this.createHasOneRepositoryFactoryFor('eventLocation', eventLocationRepositoryGetter);
    this.registerInclusionResolver('eventLocation', this.eventLocation.inclusionResolver);
    this.pictures = this.createHasManyRepositoryFactoryFor('pictures', picturesRepositoryGetter,);
    this.registerInclusionResolver('pictures', this.pictures.inclusionResolver);
  }
}
