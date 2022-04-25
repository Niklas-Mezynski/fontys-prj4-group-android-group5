import { User } from '../../models/user.model';
import { Client, expect } from '@loopback/testlab';
import { ProjectX } from '../..';
import { setupApplication } from './test-helper';
import { UserLocationRepository, UserRepository } from '../../repositories';
import sinon from 'sinon';
import { UserController } from '../../controllers';
import { JWTService, MyUserService } from '@loopback/authentication-jwt';
import { SecurityBindings, securityId, UserProfile } from '@loopback/security';
import { authenticate, TokenService } from '@loopback/authentication';
import { givenEmptyDatabase } from '../helpers/database.helpers';

describe('UserController', () => {
    let userRepository: UserRepository;
    let userLocationRepository: UserLocationRepository;
    beforeEach(givenStubbedRepository);

    // your unit tests
    it('Register a new user', async () => {
        const controller = new UserController(
            sinon.createStubInstance(JWTService),
            sinon.createStubInstance(MyUserService),
            { [securityId]: "test123" },
            userRepository,
            userLocationRepository,
        )
        let newUser = new User({
            email: 'd@w1235342.com',
            password: '123456789',
            firstName: 'Dfweiopkf',
            lastName: 'Wfewfwfwe',
            nick_name: 'Dwfwewf',
            birth_date: '2002-04-19T09:51:19.229Z',
        });

        let res = await controller.create(newUser);
        expect(res.id.length > 10).true();
    });

    function givenStubbedRepository() {
        userRepository = sinon.createStubInstance(UserRepository);
        userLocationRepository = sinon.createStubInstance(UserLocationRepository);
    }
});
