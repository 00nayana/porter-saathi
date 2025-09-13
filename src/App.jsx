import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import { Container, Box, Button } from '@mui/material';
import HomePageHeader from './components/HomePageHeader';
import RegistrationForm from './components/RegistrationForm';
import PenaltyQuery from './components/PenaltyQueryPage';
import EarningsStatistics from './components/EarningsStatistics';
import NavigateMeComponent from './components/NavigateMeComponent';

const App = () => {
  const [isRegistered, setIsRegistered] = useState(false);

  useEffect(() => {
    const stored = localStorage.getItem('isRegistered');
    if (stored === 'true') setIsRegistered(true);
  }, []);

  const handleRegistrationSuccess = () => {
    localStorage.setItem('isRegistered', 'true');
    setIsRegistered(true);
  };

  return (
    <Router>
      <Container maxWidth="sm" sx={{ padding: 2 }}>
        <Box sx={{ textAlign: 'center', mb: 4 }}>
          <HomePageHeader />
        </Box>

        <Routes>
          {!isRegistered ? (
            <Route path="*" element={<RegistrationForm onSuccess={handleRegistrationSuccess} />} />
          ) : (
            <>
              <Route path="/" element={<Navigate to="/menu" />} />
              <Route path="/menu" element={<Menu />} />
              <Route path="/penalty" element={<PenaltyQueryPageWrapper />} />
              <Route path="/statistics" element={<EarningsStatisticsWrapper />} />
              <Route path="/navigateme" element={<NavigateMeComponent />} />

            </>
          )}
        </Routes>
      </Container>
    </Router>
  );
};

const Menu = () => {
  const navigate = useNavigate();

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <Button variant="contained" onClick={() => navigate('/penalty')}>
        📦 Query Assistant
      </Button>
      <Button variant="contained" onClick={() => navigate('/statistics')}>
        📊 Earnings Statistics
      </Button>
      <Button variant="contained" onClick={() => navigate('/navigateme')}>
        📊Help Navigate
      </Button>
    </Box>
  );
};

const PenaltyQueryPageWrapper = () => {
  const navigate = useNavigate();

  return (
    <Box>
      <Button variant="outlined" sx={{ mb: 2 }} onClick={() => navigate(-1)}>
        ← Back
      </Button>
      <PenaltyQuery />
    </Box>
  );
};

const EarningsStatisticsWrapper = () => {
  const navigate = useNavigate();

  return (
    <Box>
      <Button variant="outlined" sx={{ mb: 2 }} onClick={() => navigate(-1)}>
        ← Back
      </Button>
      <EarningsStatistics />
    </Box>
  );
};

export default App;
