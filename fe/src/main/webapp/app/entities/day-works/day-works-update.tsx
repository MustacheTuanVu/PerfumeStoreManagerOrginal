import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMember } from 'app/shared/model/member.model';
import { getEntities as getMembers } from 'app/entities/member/member.reducer';
import { getEntity, updateEntity, createEntity, reset } from './day-works.reducer';
import { IDayWorks } from 'app/shared/model/day-works.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDayWorksUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DayWorksUpdate = (props: IDayWorksUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { dayWorksEntity, members, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/day-works' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getMembers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...dayWorksEntity,
        ...values,
        member: members.find(it => it.id.toString() === values.memberId.toString()),
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
          <h2 id="perfumeStoreManagerApp.dayWorks.home.createOrEditLabel" data-cy="DayWorksCreateUpdateHeading">
            <Translate contentKey="perfumeStoreManagerApp.dayWorks.home.createOrEditLabel">Create or edit a DayWorks</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : dayWorksEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="day-works-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="day-works-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="workIDLabel" for="day-works-workID">
                  <Translate contentKey="perfumeStoreManagerApp.dayWorks.workID">Work ID</Translate>
                </Label>
                <AvField id="day-works-workID" data-cy="workID" type="text" name="workID" />
              </AvGroup>
              <AvGroup>
                <Label id="userIDLabel" for="day-works-userID">
                  <Translate contentKey="perfumeStoreManagerApp.dayWorks.userID">User ID</Translate>
                </Label>
                <AvField id="day-works-userID" data-cy="userID" type="text" name="userID" />
              </AvGroup>
              <AvGroup>
                <Label id="dayWorkLabel" for="day-works-dayWork">
                  <Translate contentKey="perfumeStoreManagerApp.dayWorks.dayWork">Day Work</Translate>
                </Label>
                <AvField id="day-works-dayWork" data-cy="dayWork" type="string" className="form-control" name="dayWork" />
              </AvGroup>
              <AvGroup>
                <Label for="day-works-member">
                  <Translate contentKey="perfumeStoreManagerApp.dayWorks.member">Member</Translate>
                </Label>
                <AvInput id="day-works-member" data-cy="member" type="select" className="form-control" name="memberId">
                  <option value="" key="0" />
                  {members
                    ? members.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/day-works" replace color="info">
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
  members: storeState.member.entities,
  dayWorksEntity: storeState.dayWorks.entity,
  loading: storeState.dayWorks.loading,
  updating: storeState.dayWorks.updating,
  updateSuccess: storeState.dayWorks.updateSuccess,
});

const mapDispatchToProps = {
  getMembers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DayWorksUpdate);
