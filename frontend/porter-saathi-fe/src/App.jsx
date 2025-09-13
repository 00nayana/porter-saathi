import React, { useState } from 'react';
import { Box, Button, Container, Typography } from '@mui/material';
import EarningsStatistics from './components/EarningsStatistics';
import PenaltyQuery from './components/PenaltyQueryPage';
import RegistrationForm from './components/RegistrationForm';
import HomePageHeader from './components/HomePageHeader';

const App = () => {
  const [isRegistered, setIsRegistered] = useState(false);
  const [currentPage, setCurrentPage] = useState('home');

  const handleRegistrationSuccess = () => {
    setIsRegistered(true);
    setCurrentPage('menu');
  };

  return (
    <Container maxWidth="sm" sx={{ padding: 2 }}>
      <Box sx={{ textAlign: 'center', mb: 4 }}>
        <HomePageHeader />
      </Box>

      {!isRegistered ? (
        <RegistrationForm onSuccess={handleRegistrationSuccess} />
      ) : (
        <>
          {currentPage === 'menu' && (
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <Typography variant="h6" sx={{ mb: 2 }}>
                Registration Successful! 🎉
                <br />
                You can now access the following options:
              </Typography>
              <Button variant="contained" onClick={() => setCurrentPage('penalty')}>
                📦 Penalty Query
              </Button>
              <Button variant="contained" onClick={() => setCurrentPage('statistics')}>
                📊 Earnings Statistics
              </Button>
            </Box>
          )}

          {currentPage === 'penalty' && (
            <Box>
              <Button variant="outlined" sx={{ mb: 2 }} onClick={() => setCurrentPage('menu')}>
                ← Back
              </Button>
              <PenaltyQuery onBack={() => setCurrentPage('menu')} />
            </Box>
          )}

          {currentPage === 'statistics' && (
            <Box>
              <Button variant="outlined" sx={{ mb: 2 }} onClick={() => setCurrentPage('menu')}>
                ← Back
              </Button>
              <EarningsStatistics />
            </Box>
          )}
        </>
      )}
    </Container>
  );
};

export default App;
