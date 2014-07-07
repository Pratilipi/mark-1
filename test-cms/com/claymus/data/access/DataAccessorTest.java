package com.claymus.data.access;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.claymus.data.transfer.BlobEntry;
import com.claymus.data.transfer.BlobEntry.Source;
import com.claymus.data.transfer.User;
import com.claymus.data.transfer.UserRole;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class DataAccessorTest {

	private final LocalServiceTestHelper helper =
			new LocalServiceTestHelper( new LocalDatastoreServiceTestConfig() );
	
	@Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
	

    @Test
    public void testUser() {
    	
    	String id = "id";
    	String password = "password";
    	String firstName = "firstName";
    	String lastName = "lastName";
    	String nickName = "nickName";
    	String email = "email";
    	String phone = "phone";
    	Date signUpDate = new Date();
    	
    	DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
    	
    	User user = dataAccessor.newUser();
    	user.setId( id );
    	user.setPassword( password );
    	user.setFirstName( firstName );
    	user.setLastName( lastName );
    	user.setNickName( nickName );
    	user.setEmail( email );
    	user.setPhone( phone );
    	user.setSignUpDate( signUpDate );
    	
    	user = dataAccessor.createUser( user );
    	dataAccessor.destroy();
    	
    	dataAccessor = DataAccessorFactory.getDataAccessor();
    	user = dataAccessor.getUser( id );
    	
    	Assert.assertNotNull( user );
    	Assert.assertEquals( id, user.getId() );
    	Assert.assertEquals( password, user.getPassword() );
    	Assert.assertEquals( firstName, user.getFirstName() );
    	Assert.assertEquals( lastName, user.getLastName() );
    	Assert.assertEquals( nickName, user.getNickName() );
    	Assert.assertEquals( email, user.getEmail() );
    	Assert.assertEquals( phone, user.getPhone() );
    	Assert.assertEquals( signUpDate, user.getSignUpDate() );
    	
    	dataAccessor.destroy();
    	
    }
    
    @Test
    public void testUserRole() {
    	
    	Long id = null;
    	String name = "name";
    	
    	DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
    	
    	UserRole userRole = dataAccessor.newUserRole();
    	userRole.setName( name );
    	
    	userRole = dataAccessor.createOrUpdateUserRole( userRole );
    	id = userRole.getId();
    	dataAccessor.destroy();
    	
    	dataAccessor = DataAccessorFactory.getDataAccessor();
    	userRole = dataAccessor.getUserRole( id );
    	
    	Assert.assertNotNull( userRole );
    	Assert.assertEquals( id, userRole.getId() );
    	Assert.assertEquals( name, userRole.getName() );
    	
    	dataAccessor.destroy();
    	
    }
    
    @Test
    public void testBlobEntry() {

    	String name = "name";

    	Source source_1 = Source.GOOGLE_APP_ENGINE;
    	String blobId_1 = "bolbId_1";
    	Date creationDate_1 = new Date();
    	
    	Source source_2 = Source.GOOGLE_CLOUD_STORAGE;
    	String blobId_2 = "bolbId_2";
    	Date creationDate_2 = new Date( new Date().getTime() + 100 );
    	
    	DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

    	BlobEntry blobEntry = dataAccessor.newBlobEntry();
    	blobEntry.setName( name );
    	blobEntry.setSource( source_1 );
    	blobEntry.setBlobId( blobId_1 );
    	blobEntry.setCreationDate( creationDate_1 );
    	
    	blobEntry = dataAccessor.createBlobEntry( blobEntry );

    	blobEntry = dataAccessor.newBlobEntry();
    	blobEntry.setName( name );
    	blobEntry.setSource( source_2 );
    	blobEntry.setBlobId( blobId_2 );
    	blobEntry.setCreationDate( creationDate_2 );
    	
    	blobEntry = dataAccessor.createBlobEntry( blobEntry );
    	dataAccessor.destroy();

    	dataAccessor = DataAccessorFactory.getDataAccessor();
    	blobEntry = dataAccessor.getBlobEntry( name );
    	
    	Assert.assertNotNull( blobEntry );
    	Assert.assertEquals( name, blobEntry.getName() );
    	Assert.assertEquals( source_2, blobEntry.getSource() );
    	Assert.assertEquals( blobId_2, blobEntry.getBlobId() );
    	Assert.assertEquals( creationDate_2, blobEntry.getCreationDate() );
    	
    	dataAccessor.destroy();
    	
    }
	
}