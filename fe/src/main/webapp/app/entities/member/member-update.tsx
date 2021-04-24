import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IStores } from 'app/shared/model/stores.model';
import { getEntities as getStores } from 'app/entities/stores/stores.reducer';
import { IRoles } from 'app/shared/model/roles.model';
import { getEntities as getRoles } from 'app/entities/roles/roles.reducer';
import { getEntity, updateEntity, createEntity, reset } from './member.reducer';
import { IMember } from 'app/shared/model/member.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMemberUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MemberUpdate = (props: IMemberUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { memberEntity, stores, roles, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/member' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getStores();
    props.getRoles();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...memberEntity,
        ...values,
        stores: stores.find(it => it.id.toString() === values.storesId.toString()),
        roles: roles.find(it => it.id.toString() === values.rolesId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="perfumeStoreManagerApp.member.home.createOrEditLabel" data-cy="MemberCreateUpdateHeading">
            <Translate contentKey="perfumeStoreManagerApp.member.home.createOrEditLabel">Create or edit a Member</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : memberEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="member-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="member-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="userIDLabel" for="member-userID">
                  <Translate contentKey="perfumeStoreManagerApp.member.userID">User ID</Translate>
                </Label>
                <AvField id="member-userID" data-cy="userID" type="text" name="userID" />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="member-name">
                  <Translate contentKey="perfumeStoreManagerApp.member.name">Name</Translate>
                </Label>
                <AvField id="member-name" data-cy="name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="phoneLabel" for="member-phone">
                  <Translate contentKey="perfumeStoreManagerApp.member.phone">Phone</Translate>
                </Label>
                <AvField id="member-phone" data-cy="phone" type="text" name="phone" />
              </AvGroup>
              <AvGroup>
                <Label id="emailLabel" for="member-email">
                  <Translate contentKey="perfumeStoreManagerApp.member.email">Email</Translate>
                </Label>
                <AvField id="member-email" data-cy="email" type="text" name="email" />
              </AvGroup>
              <AvGroup>
                <Label id="roleIDLabel" for="member-roleID">
                  <Translate contentKey="perfumeStoreManagerApp.member.roleID">Role ID</Translate>
                </Label>
                <AvField id="member-roleID" data-cy="roleID" type="text" name="roleID" />
              </AvGroup>
              <AvGroup>
                <Label id="storeIDLabel" for="member-storeID">
                  <Translate contentKey="perfumeStoreManagerApp.member.storeID">Store ID</Translate>
                </Label>
                <AvField id="member-storeID" data-cy="storeID" type="text" name="storeID" />
              </AvGroup>
              <AvGroup>
                <Label id="salaryLabel" for="member-salary">
                  <Translate contentKey="perfumeStoreManagerApp.member.salary">Salary</Translate>
                </Label>
                <AvField id="member-salary" data-cy="salary" type="string" className="form-control" name="salary" />
              </AvGroup>
              <AvGroup>
                <Label for="member-stores">
                  <Translate contentKey="perfumeStoreManagerApp.member.stores">Stores</Translate>
                </Label>
                <AvInput id="member-stores" data-cy="stores" type="select" className="form-control" name="storesId">
                  <option value="" key="0" />
                  {stores
                    ? stores.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="member-roles">
                  <Translate contentKey="perfumeStoreManagerApp.member.roles">Roles</Translate>
                </Label>
                <AvInput id="member-roles" data-cy="roles" type="select" className="form-control" name="rolesId">
                  <option value="" key="0" />
                  {roles
                    ? roles.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/member" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  stores: storeState.stores.entities,
  roles: storeState.roles.entities,
  memberEntity: storeState.member.entity,
  loading: storeState.member.loading,
  updating: storeState.member.updating,
  updateSuccess: storeState.member.updateSuccess,
});

const mapDispatchToProps = {
  getStores,
  getRoles,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MemberUpdate);
