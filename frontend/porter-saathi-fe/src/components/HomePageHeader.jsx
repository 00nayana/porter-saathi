import { Box, Typography } from '@mui/material';

function HomePageHeader() {
  return (
    <Box sx={{ display: 'flex', alignItems: 'center', padding: '1rem', backgroundColor: '#1976d2', color: 'white' }}>
      <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M3 21v-2a4 4 0 0 1 4-4h10a4 4 0 0 1 4 4v2" />
        <circle cx="12" cy="7" r="4" />
        <rect x="2" y="14" width="20" height="5" rx="2" ry="2" />
      </svg>
      <Typography variant="h5" ml={2}>
        Porter Saathi
      </Typography>
    </Box>
  );
}

export default HomePageHeader;
